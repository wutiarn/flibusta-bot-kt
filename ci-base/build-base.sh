#!/bin/bash -ve

cd "$(dirname "$0")"

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
BASE_IMAGE_NAME="wutiarn/ci-base:$VERSION_TAG"

CACHE_DIR="~/docker"
IMG_TAR_PATH="$CACHE_DIR/$VERSION_TAG.tar"

if [[ -e "$IMG_TAR_PATH" ]];
    then docker load -i "$IMG_TAR_PATH"
else
    rm -rf "$CACHE_DIR"
    mkdir -p "$CACHE_DIR"
    docker build -t "$BASE_IMAGE_NAME" -f Dockerfile.base .
    docker save "$BASE_IMAGE_NAME" > "$IMG_TAR_PATH"
fi