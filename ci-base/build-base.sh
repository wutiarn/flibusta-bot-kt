#!/bin/bash -e

cd "$(dirname "$0")"

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
BASE_IMAGE_NAME="wutiarn/ci-base"

echo "Dockerfile hash is $VERSION_TAG"

CACHE_DIR="~/docker"
IMG_TAR_PATH="$CACHE_DIR/$VERSION_TAG.tar"

if [[ -e "$IMG_TAR_PATH" ]];
    echo "Found cached image. Restoring..."
    then docker load -i "$IMG_TAR_PATH"
else
    echo "No cache image found. Building new one..."
    rm -rf "$CACHE_DIR"
    mkdir -p "$CACHE_DIR"
    docker build -t "$BASE_IMAGE_NAME" -f Dockerfile.base .
    echo "Saving built image..."
    docker save "$BASE_IMAGE_NAME" > "$IMG_TAR_PATH"
fi

echo "DONE"