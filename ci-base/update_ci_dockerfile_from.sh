#!/bin/bash -e

BASEDIR=$(pwd)

cd "$(dirname "$0")"

VERSION_TAG=$(cat VERSION_TAG.txt)

sed -i "" "s/\#CI_BASE_IMAGE_HERE/wutiarn\/ci-base-cache:$VERSION_TAG/" "$BASEDIR/$1"