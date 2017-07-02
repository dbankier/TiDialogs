#!/bin/bash

APPID=yy.tidialogs
VERSION=3.0.3

ant clean; rm -rf build/*;ant ;  unzip -uo  dist/$APPID-android-$VERSION.zip  -d  ~/Library/Application\ Support/Titanium/
unzip -uo  dist/$APPID-android-$VERSION.zip  -d  /Library/Application\ Support/Titanium/
