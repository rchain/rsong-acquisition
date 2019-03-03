#!/bin/bash

acq=~/dev/workspaces/workspace-rchain/rsong-acquisition
proxy=~/dev/workspaces/workspace-rchain/rsong-proxy
ivy2=~/.ivy2/local/coop.rchain
libs="casper_2.12/0.1.0-SNAPSHOT/jars/casper_2.12.jar \
    comm_2.12/0.1/jars/comm_2.12.jar \
    crypto_2.12/0.1.0-SNAPSHOT/jars/crypto_2.12.jar \
    models_2.12/0.1.0-SNAPSHOT/jars/models_2.12.jar \
    rholang_2.12/0.1.0-SNAPSHOT/jars/rholang_2.12.jar \
    rspace_2.12/0.2.1-SNAPSHOT/jars/rspace_2.12.jar \
    shared_2.12/0.1/jars/shared_2.12.jar"
echo "acq = $acq"
echo "proxy = $proxy"
echo "libs = $libs"
echo "ivy2 = $ivy2"

for i in $libs;do
    yes | cp -rf $ivy2/$i $acq/lib
    yes | cp -rf $ivy2/$i $proxy/lib
done
