#!/bin/bash -e

cd "$(dirname "$0")"

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
BASE_IMAGE_NAME="ci-base"

echo "Dockerfile hash is $VERSION_TAG"

CACHE_DIR="~/docker"
IMG_TAR_PATH="$CACHE_DIR/$VERSION_TAG.tar"

if [[ -e "$IMG_TAR_PATH" ]]; then
    echo "Found cached image. Restoring..."
    docker load -i "$IMG_TAR_PATH"
else
    echo "No cache image found. Building new one..."
    rm -rf "$CACHE_DIR"
    mkdir -p "$CACHE_DIR"
    docker build -t "$BASE_IMAGE_NAME" -f Dockerfile.base .
    echo "Saving built image ($BASE_IMAGE_NAME) to $IMG_TAR_PATH..."
    docker save "$BASE_IMAGE_NAME" > "$IMG_TAR_PATH"
fi

docker history "$BASE_IMAGE_NAME"

echo "DONE"