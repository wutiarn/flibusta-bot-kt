#!/bin/bash -e

cd "$(dirname "$0")"

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
IMAGE_ID="wutiarn/ci-base-cache:$VERSION_TAG"

echo -n "$VERSION_TAG" > VERSION_TAG.txt

echo "Dockerfile hash is $VERSION_TAG"

CACHE_DIR="$HOME/docker"
IMG_TAR_PATH="$CACHE_DIR/$VERSION_TAG.tar"

if [[ -e "$IMG_TAR_PATH" ]]; then
    echo "Found cached image. Restoring..."
    docker load -i "$IMG_TAR_PATH"
else
    echo "No cache image found. Building new one..."
    rm -rf "$CACHE_DIR"
    mkdir -p "$CACHE_DIR"
    docker build -t "$IMAGE_ID" -f Dockerfile.base .

    echo "Pushing as $IMAGE_ID..."
    docker push "$IMAGE_ID" | cat

    echo "Saving built image ($IMAGE_ID) to $IMG_TAR_PATH..."
    docker save "$IMAGE_ID" > "$IMG_TAR_PATH"
fi

docker history "$IMAGE_ID"

echo "DONE"