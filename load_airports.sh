#!/bin/bash

# Execute class RxLoader to load the CosmosDB airports collection.
# Chris Joakim, Microsoft, 2018/06/17

echo 'Define and export the classpath as $CP ...'
source classpath.sh

function="load_airports"
infile="data/public/world_airports_flat.json"
usedb="false"
dbname="dev"
collname="airports" 
pause=10
outfile="tmp/load_airports.txt"

echo 'Invoking function '$function

java -classpath $CP com.chrisjoakim.springboot1.utils.CosmosDbUtil --function $function --infile $infile --use-db $usedb --dbname $dbname --collname $collname --pause $pause 

# &> $outfile

echo 'done'