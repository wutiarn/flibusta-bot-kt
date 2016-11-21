#!/usr/bin/env bash -ve

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
BASE_IMAGE_NAME="wutiarn/ci-base:$VERSION_TAG"

CACHE_PATH="~/docker/$VERSION_TAG.tar"

if [[ -e "$CACHE_PATH" ]];
    then docker load -i "$CACHE_PATH"
else
    rm -rf ~/docker
    mkdir -p ~/docker
    docker build -t "$BASE_IMAGE_NAME" -f Dockerfile.base .
    docker save "$BASE_IMAGE_NAME" > "$CACHE_PATH"
fi