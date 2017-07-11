# JBlackboxUtils
This repository mainly hosts a Java implementation of a blackbox reader.
The blackbox formats supported shall generally be Cleanflight and Betaflight formats.

This project is separated in multiple packages:
* *sampled* contains base-classes for handling sample based data
* *blackbox* contains only the part for reading and interpreting blackbox-data. It is based on *sampled*
* *propsynth* contains an experimental project for synthesizing propeller noise. It is based on *sampled*

## Blackbox Reader
The blackbox reader is capable of reading Blackbox flight data as it has been defined by Nicholas Sherlock.

## Audio Synthesizer
As a first and very experimental usecase, an audio synthesizer exists was created. This synthesizer will read blackbox data using the
blackbox reader and synthesizes propeller noise as audio. This can later be used to recreate the audio stream of recorded flight footage for
example for DVR recordings.

The audio synthesis basically calculates the propeller rotation based on the motor outputs from the log for every target-audio-sample. The calcualted propeller position then is muliplied by the number of propeller-blades and fed into a sin() function to get the base frequency for the sample. Additionally a range of harmonic frequencies are added.
This audio is calcuated for all propellers. These tracks then are mixed down to the stereo-channels and either stored as wave-file or played back directly.