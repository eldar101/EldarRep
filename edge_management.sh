#!/bin/bash
# This is meant to be run from the provisionh in the terminal, after openshift deployment, then exit and work from the kni user.

# Install OC on hypervisor by copying it from the provisionhost
scp /home/kni/openshift-client-linux-amd64-rhel8-4.16.0-0.nightly-2024-08-01-082745.tar.gz root@10.46.4.68:/root/.
# alter to the name of to your titan
ssh root@titan52 
# This sets openshift in your kni user, enables it, then returns to the kni user
tar -xvf cp oc openshift-client-linux-amd64-rhel8-4.16.0-0.nightly-2024-08-01-082745.tar.gz
cp oc /home/kni/.local/bin
su - kni 
# Add oc autocompletion for easy use
oc completion bash > oc_bash_completion
sudo cp oc_bash_completion /etc/bash_completion.d/
export KUBECONFIG=/home/kni/clusterconfigs/auth/kubeconfig

# Install helm by using snapd
curl -L https://mirror.openshift.com/pub/openshift-v4/clients/helm/latest/helm-linux-amd64 -o /usr/local/bin/helm
chmod +x /usr/local/bin/helm
sudo dnf install -y https://dl.fedoraproject.org/pub/epel/epel-release-latest-8.noarch.rpm
sudo dnf -y upgrade
sudo snap install helm --classic -y

# Set up flightctl repo
git config --global http.sslVerify false
git clone https://github.com/flightctl/flightctl
cd flightctl
git pull

# Install flightctl prequisites: go (>1.21), Cgo, openssl, openssl-devel,podman-compose
sudo yum install -y glibc-devel.i686 --allowerasing
wget https://go.dev/dl/go1.22.5.linux-386.tar.gz
sudo tar -C /usr/local -xzf go1.22.5.linux-386.tar.gz
export PATH=$PATH:/usr/local/go/bin
sudo yum install -y openssl
sudo yum install -y openssl-devel
sudo yum install -y podman-compose
systemctl --user enable --now podman.socket

# Create a modified values.yaml
cp /home/kni/flightctl/deploy/helm/flightctl/values.yaml /home/kni/flightctl/deploy/helm/flightctl/values_new.yaml
sudo perl -pi -e 's/api\.flightctl\.example\.com/api.apps.ocp-edge-cluster-0.qe.lab.redhat.com/g; s/agent-api\.flightctl\.example\.com/agent-api.apps.ocp-edge-cluster-0.qe.lab.redhat.com/g; s/agent-grpc\.flightctl\.example\.com/agent-grpc.apps.ocp-edge-cluster-0.qe.lab.redhat.com/g; s|grpcs://agent-grpc\.flightctl\.example\.com|grpcs://agent-grpc.apps.ocp-edge-cluster-0.qe.lab.redhat.com|g; s/aws-ebs/standard/g; s/aws-efs-tier-c4/standard-multiaccess/g' /home/kni/flightctl/deploy/helm/flightctl/values_new.yaml

# Create the namespaces
oc create namespace flightctl-internal 
oc create namespace flightctl-external

# Install flightctl with the new values.yaml
helm install --values /home/kni/flightctl/deploy/helm/flightctl/values_new.yaml flightctl ./deploy/helm/flightctl/
mkdir -p ~/.flightctl/certs

# Modify certs
API_POD=$(oc get pod -n flightctl-external -l flightctl.service=flightctl-api --no-headers -o custom-columns=":metadata.name" | head -1 )

for f in certs/{ca.crt,client-enrollment.crt,client-enrollment.key} client.yaml; do
	# a kubectl cp would be more efficient, but tar is not available on the image, and we don't want
	# to switch from ubi9-micro just for tar
	oc exec -n flightctl-external "${API_POD}"  -- cat /root/.flightctl/$f > ~/.flightctl/$f
  done

  chmod og-rwx ~/.flightctl/certs/*.key

# Run make and login
Make
/bin/flightctl login https://api.apps.ocp-edge-cluster-0.qe.lab.redhat.com

# Verify devices and pods
oc get pods -n flightctl-internal 
oc get pods -n flightctl-external
./bin/flightctl get device 

