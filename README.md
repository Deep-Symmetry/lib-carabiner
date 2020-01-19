# lib-carabiner

<img align="right" width="150" src="doc/assets/carabiner.jpg">

Java wrapper for the
[Carabiner](https://github.com/Deep-Symmetry/carabiner#carabiner)
bridge to [Ableton Link](https://www.ableton.com/en/link/).

[![License](https://img.shields.io/badge/License-Eclipse%20Public%20License%202.0-blue.svg)](#licenses)

lib-carabiner allows you to embed Carabiner inside a Java project, and
automatically run it when needed, as long as you are running on a
supported operating system and processor architecture. The combinations
currently supported are:

* Linux Intel 64 bit
* macOS Intel 64 bit
* Raspberry Pi (Linux ARM 32 bit)
* Windows Intel 64 bit

## Installing

lib-carabiner is available through Maven Central, so to use it in your
Maven project, all you need is to include the appropriate dependency.

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.deepsymmetry/lib-carabiner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.deepsymmetry/lib-carabiner)

Click the **maven central** badge above to view the repository entry
for lib-carabiner. The proper format for including the latest release as a
dependency in a variety of tools, including Leiningen if you are using
lib-carabiner from Clojure, can be found in the **Dependency Information**
section.

lib-carabiner uses [slf4j](http://www.slf4j.org/manual.html) to allow
you to integrate it with whatever Java logging framework your project
is using, so you will need to include the appropriate slf4j binding on
your class path.

It also uses [edn-java](https://github.com/bpsm/edn-java#edn-java) to
parse and build the Extensible Data Notation format messages used by
Carabiner.

## Usage

See the [API Documentation](http://deepsymmetry.org/lib-carabiner/apidocs/)
for full details, but here is a nutshell guide:

The package `org.deepsymmetry.libcarabiner` offers a `Runner` singleton
that manages an embedded native instance of Carabiner for you. Call
[`getInstance()`](https://deepsymmetry.org/lib-carabiner/apidocs/org/deepsymmetry/libcarabiner/Runner.html#getInstance())
to get that singleton, and then you can call
[`canRunCarabiner()`](https://deepsymmetry.org/lib-carabiner/apidocs/org/deepsymmetry/libcarabiner/Runner.html#canRunCarabiner())
to determine whether a native build of Carabiner is offered for the
current operating system and processor architecture. If so, you can call
[`start()`](https://deepsymmetry.org/lib-carabiner/apidocs/org/deepsymmetry/libcarabiner/Runner.html#start())
to extract and run it, and  
[`stop()`](https://deepsymmetry.org/lib-carabiner/apidocs/org/deepsymmetry/libcarabiner/Runner.html#stop())
when you want to terminate it.

## Licenses

<img align="right" alt="Deep Symmetry"
 src="doc/assets/DS-logo-bw-200-padded-left.png"
 width="216" height="123">

Copyright © 2020 [Deep Symmetry, LLC](http://deepsymmetry.org)

Distributed under the [Eclipse Public License
2.0](https://opensource.org/licenses/EPL-2.0). By using this software
in any fashion, you are agreeing to be bound by the terms of this
license. You must not remove this notice, or any other, from this
software. A copy of the license can be found in
[epl-2.0.md](https://github.com/Deep-Symmetry/afterglow/blob/master/epl-2.0.md)
within this project.

The included copies of Carabiner are distributed under the [GNU
General Public License, version
2](https://opensource.org/licenses/GPL-2.0). A copy of the license can be found in
[gpl-2.0.md](https://github.com/Deep-Symmetry/afterglow/blob/master/gpl-2.0.md)
within this project.
