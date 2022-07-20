#!/bin/bash
git config --global http.sslVerify false
git clone https://gitlab.cee.redhat.com/eweiss/ocp-edge-auto.git
cd ocp-edge-auto
virtualenv .venv
. .venv/bin/activate
pip install -r requirements.txt
cp ../clusterconfigs/auth/kubeconfig .
cp ../install-config.yaml .
export KUBECONFIG=kubeconfig
export ASSISTED_INSTALLER=false
export SNO=false
export ENVIRONMENT="Virtual"
echo '[core]
        repositoryformatversion = 0
        filemode = true
        bare = false
        logallrefupdates = true
[remote "origin"]
        url = https://gitlab.cee.redhat.com/eweiss/ocp-edge-auto.git
        fetch = +refs/heads/*:refs/remotes/origin/*
[branch "master"]
        remote = origin
        merge = refs/heads/master
[remote "upstream"]
        url = https://gitlab.cee.redhat.com/ocp-edge-qe/ocp-edge-auto.git
        fetch = +refs/heads/*:refs/remotes/upstream/*'  >> /home/kni/ocp-edge-auto/.git/config
git config pull.rebase false
git config --global user.name "Eldar Weiss"
git checkout master
git fetch upstream
git merge upstream/master
git pull
git config --global user.email "eweiss@redhat.com"
git config --global credential.helper cache
sudo yum install mlocate -y; sudo updatedb; sudo yum install vim -y;export KUBECONFIG=/home/kni/clusterconfigs/auth/kubeconfig
