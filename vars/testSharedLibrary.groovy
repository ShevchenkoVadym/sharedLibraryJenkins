#!/usr/bin/env groovy

def call(string) {

    echo "Into Shared Method"
    echo "Print ${string}"
    echo "Print env var ${env.BUILD_URL}"

    return this
}
