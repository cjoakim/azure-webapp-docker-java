
import csv
import json
import sys
import time
import os
import random
import requests

from time import gmtime, strftime

# Chris Joakim, Microsoft, 2018/06/17
# python http_client.py get_id 87ed3026-9539-4c49-863d-4e54e60a8316
# python http_client.py get_id bc06f01f-3f86-49ee-86f0-1f3f8639ee2d 
# python http_client.py post_airport 

headers = {'Content-Type':'application/json'}

def target_url():
    return 'http://localhost:8080/cosmosdb/zipcodes/'


if __name__ == "__main__":

    if len(sys.argv) > 1:
        func  = sys.argv[1].lower()

        if func == 'get_id':
            id = sys.argv[2]
            url = "{}{}".format(target_url(), id)
            print(url)
            r = requests.get(url, headers=headers)
            print(r)
            print(r.text)

        elif func == 'post_airport':
            print(func)
            airport = dict()
            airport['pk'] = 'test'
            airport['time'] = str(time.time())
            url = "{}".format(target_url())
            print("url: {}".format(url))
            r = requests.post(url, headers=headers, data=json.dumps(airport))
            print(r)
            print(r.text)

        else:
            print("undefined function: {}".format(func))

