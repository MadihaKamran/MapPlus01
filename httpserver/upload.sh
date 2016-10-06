#!/bin/bash
sudo scp -r  -i ../../maps.pem  ./*  ubuntu@52.45.41.223:/home/ubuntu/server
ssh -i ../../maps.pem ubuntu@52.45.41.223

