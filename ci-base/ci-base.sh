#!/bin/bash -e

cd "$(dirname "$0")"

VERSION_TAG="$(sha1sum Dockerfile.base | sha1sum | cut -d' ' -f1)"
IMAGE_ID="wutiarn/ci-base-cache:$VERSION_TAG"

echo -n "$VERSION_TAG" > VERSION_TAG.txt

echo "Dockerfile hash is $VERSION_TAG"

echo "Pulling $IMAGE_ID..."
if docker pull "$IMAGE_ID"; then
    echo "Pull completed"
else
    echo "No cache image found. Building new one...";
    docker build -t "$IMAGE_ID" -f Dockerfile.base .

    echo "Pushing as $IMAGE_ID..."
    docker push "$IMAGE_ID" | cat
fi;

echo "History:"
docker history "$IMAGE_ID"

echo "Pushing as $IMAGE_ID..."
docker push "$IMAGE_ID" | cat

echo "DONE"