#!/usr/bin/env groovy

def call(String buildStatus = 'FAILURE', String log, buildNumberSQM, version_sqm_engine, sqm_core_node) {
    env.buildStatus = buildStatus ?: 'SUCCESS'

    packageName = 'Unknown'
    try {
        packageNamePathArray = log.split('Downloading the latest SQM package...')[1].trim().split(" ")[0].split("/")
        fullPackageName = packageNamePathArray[packageNamePathArray.length - 1]
        packageName = fullPackageName.substring(0, fullPackageName.indexOf('tar.gz') + 6)
    } catch (Exception ignored) {}

    def subject = "SQM-Engine Update Automation of the latest build ${buildNumberSQM} / ${version_sqm_engine} on ${sqm_core_node} - ${env.buildStatus}"
    def details = """<!DOCTYPE html>
                    <head>
                      <title>Build report</title>
                      <style type="text/css">
                        body{margin: 0px;padding: 15px;}
                        body, td, th{font-family: "Lucida Grande", "Lucida Sans Unicode", Helvetica, Arial, Tahoma, sans-serif;font-size: 10pt;}
                        th{text-align: left;}
                        h1{margin-top: 0px;}
                        li{line-height: 15pt;}
                        .change-add{color: #272;}
                        .change-delete{color: #722;}
                        .change-edit{color: #247;}
                        .grayed{color: #AAA;}
                        .error{color: #A33;}
                        pre.console{color: #333;font-family: "Lucida Console", "Courier New";padding: 5px;line-height: 15px;background-color: #EEE;border: 1px solid #DDD;}
                      </style>
                    </head>
                    <body>
                     
                    <h1>Build ${env.buildStatus}</h1>
                    <table>
                 
                      <tr><th>Server:</th><td>${sqm_core_node}</td></tr>
                      <tr><th>Version of SQM:</th><td>${version_sqm_engine}</td></tr>
                      <tr><th>Package name:</th><td>${packageName}</td></tr>
                      <tr><th>Build number:</th><td>${buildNumberSQM}</td></tr> 
                      <tr><th>Job name:</th><td>${env.JOB_NAME}</td></tr>
                      <tr><th>Build duration:</th><td>${currentBuild.durationString.replace(' and counting', '')}</td></tr>
                      <tr><th>Build URL:</th><td><a href="${env.BUILD_URL}">${env.BUILD_URL}</a></td></tr>
                    
                    </table>
                    </body>"""
    emailext(
            subject: subject,
            body: details,
            to: 'a.vadym.shevchenko@ericsson.com',
            attachLog: true,
            mimeType: 'text/html'
    )
    return this
}
