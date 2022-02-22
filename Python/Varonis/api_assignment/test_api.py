# -*- coding: utf-8 -*-
"""
Test to check api calls in in varonis assignment
"""
import json
import pytest
import requests


URL = "http://localhost:8000"
data = {
    "data": [{"key": "key1", "val": "val1", "valType": "str"}]
}
credentials_data = {
    "username": "test",
    "password": "1234"
}
wrong_data = {
    "username": "wrong",
    "password": "1111"
}


def get_server_token(log_data):
    """
    This function gets the authorization token to the server
    Arg:
        (dict) log_data: to connect to the server
    Returns:
        (str)token: the token, empty otherwise
    """

    response = requests.post(f"{URL}/api/auth/", json=log_data)

    if response.status_code == 200:
        token = response.json()["access_token"]
        print("Authenticated successfully! token: " + token)
        return token
    else:
        print("Error! Response code: " + response.status_code)
        return None


def get_objects_ids(headers):
    """
    This function gets objects from the server
    Returns:
        (list) objects
    """

    object_ids = []
    res = requests.get(f"{URL}/api/poly/", headers=headers)
    poly_objects = json.loads(res.text)
    # Retrieving all objects
    for poly in poly_objects:
        object_ids.append(poly['object_id'])
    return object_ids


def create_obj(headers):
    """
    This function creates an object
    Returns:
        (obj) poly_object
    """

    # Call to server
    res = requests.post(f"{URL}/api/poly/", json=data, headers=headers)
    print(json.dumps(res.json(), indent=4, default=str))
    # Object loaded
    poly_object = json.loads(res.text)
    return poly_object


def delete_obj(object_id, headers):
    """
    This function deletes an object
    Args:
        object_id (int): the object we delete
        headers(dict): to connect to the server
    Returns:
        (int) response code
    """

    res = requests.delete(f"{URL}/api/poly/{object_id}", headers=headers)
    # Return answer
    return res.status_code


def test_wrong_auth():
    """Test to check connection with wrong credentials
    :return: None
    """

    # Get access token
    access_token = get_server_token(wrong_data)
    assert access_token == "", "Could connect to the server with wrong credentials"


def test_auth():
    """Test to verify we can access server with authentication
    :return: None
    """

    # Get access token
    access_token = get_server_token(credentials_data)
    assert access_token != "", "Failure in server authentication!"
