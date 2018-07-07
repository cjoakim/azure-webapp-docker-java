
import csv
import json
import sys
import time
import os
import random
import requests

from time import gmtime, strftime

# This is a Python-based HTTP client used to test the Java/SpringBoot app.
# Chris Joakim, Microsoft, 2018/07/08
#
# python http_client.py time
# python http_client.py env
# python http_client.py get_id 0cf7eb67-13d6-4e63-b232-04dd64ff6677
# python http_client.py get_id bc06f01f-3f86-49ee-86f0-1f3f8639ee2d 
# python http_client.py get_id eddd56b5-3b4c-4b50-9d56-b53b4c7b50c6 
# python http_client.py delete_id test eddd56b5-3b4c-4b50-9d56-b53b4c7b50c6
# python http_client.py delete_id test 2dcf43ae-94c4-42f8-8f43-ae94c482f88c
# python http_client.py pk CLT
# python http_client.py post_airport 
# python http_client.py update_airport 
# python http_client.py query 
# python http_client.py invoke_http_function <pk-value>

headers = {'Content-Type':'application/json'}

def base_url():
    return os.environ['AZURE_WEBAPP_URL']
    # https://cjoakim-webapp-docker-java.azurewebsites.net/
    # return 'http://localhost:8080/'

def airports_url():
    return '{}cosmosdb/airports/'.format(base_url())

def pk_url():
    return '{}cosmosdb/airports/pk/'.format(base_url())

def new_airport():
    airport = dict()
    airport['pk'] = 'test'
    airport['time'] = str(time.time())
    return airport

if __name__ == "__main__":

    if len(sys.argv) > 1:
        func = sys.argv[1].lower()

        if func == 'time':
            url = "{}time".format(base_url())
            print(url)
            r = requests.get(url, headers=headers)
            print(r)
            print(r.text)

        if func == 'env':
            secret = os.environ['AZURE_WEBAPP_SECRET']
            url = "{}/env/{}".format(base_url(), secret)
            print(url)
            r = requests.get(url, headers=headers)
            print(r)
            print(r.text)
            obj = json.loads(r.text)
            print(json.dumps(obj, sort_keys=True, indent=2))

        elif func == 'get_id':
            id = sys.argv[2]
            url = "{}{}".format(airports_url(), id)
            print(url)
            r = requests.get(url, headers=headers)
            print(r)
            print(r.text)

        elif func == 'pk':
            pk = sys.argv[2].upper()
            url = "{}{}".format(pk_url(), pk)
            print(url)
            r = requests.get(url, headers=headers)
            print(r)
            print(r.text)

        elif func == 'post_airport':
            airport = new_airport()
            url = "{}".format(airports_url())
            print("url: {}".format(url))
            r = requests.post(url, headers=headers, data=json.dumps(airport))
            print(r)
            print(r.text)

        elif func == 'update_airport':
            url1 = "{}".format(airports_url())
            print(url1)
            a1   = new_airport()
            r1   = requests.post(url1, headers=headers, data=json.dumps(a1))
            doc1 = json.loads(r1.text)
            print(doc1)
            doc1['xxx'] = str(time.time())
            id = doc1['id']

            url2 = "{}{}".format(airports_url(), id)
            print(url2)
            r2   = requests.put(url2, headers=headers, data=json.dumps(doc1))
            doc2 = json.loads(r2.text)
            print(doc2)

        elif func == 'delete_id':
            pk = sys.argv[2]
            id = sys.argv[3]
            url = "{}{}/{}".format(airports_url(), pk, id)
            print(url)
            r = requests.delete(url, headers=headers)
            print(r)
            print(r.text)

        elif func == 'query':
            query = dict()
            query['sql'] = 'SELECT * FROM c where c.pk = "BDL"'
            url = "{}query/".format(airports_url(), id)
            print("url: {}".format(url))
            r = requests.post(url, headers=headers, data=json.dumps(query))
            array = json.loads(r.text)
            print(array)

        elif func == 'invoke_http_function':
            pk = sys.argv[2]
            url = os.environ['AZURE_TEST_FUNCTION_URL']
            doc = dict()
            doc['pk'] = pk
            doc['epoch'] = str(time.time())
            doc['client'] = 'python'
            print('sending doc: {}'.format(json.dumps(doc)))
            r = requests.post(url, headers=headers, data=json.dumps(doc))
            print(r)
            print(r.text)

        else:
            print("undefined function: {}".format(func))

