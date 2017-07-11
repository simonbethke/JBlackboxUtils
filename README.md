# JBlackboxUtils
This repository mainly hosts a Java implementation of a blackbox reader.
The blackbox formats supported shall generally be Cleanflight and Betaflight formats.

## Audio Synthesizer
As a first and very experimental usecase, an audio synthesizer exists was created. This synthesizer will read blackbox data using the
blackbox reader and synthesizes propeller noise as audio. This can later be used to recreate the audio stream of recorded flight footage for
example for DVR recordings.
