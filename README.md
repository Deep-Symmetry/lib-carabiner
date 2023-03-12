# lib-carabiner

<img align="right" width="150" src="doc/assets/carabiner.png">

Java wrapper for the
[Carabiner](https://github.com/Deep-Symmetry/carabiner#carabiner)
bridge to [Ableton Link](https://www.ableton.com/en/link/).

[Licenses](#licenses)

lib-carabiner allows you to embed Carabiner inside a Java project, and
automatically run it when needed, as long as you are running on a
supported operating system and processor architecture. The combinations
currently supported are:

* Linux Intel 64 bit
* macOS Intel 64 bit and Apple Silicon (ARM 64 bit) universal binary
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

## Getting Help

<a href="http://zulip.com"><img align="right" alt="Zulip logo"
 src="doc/assets/zulip-icon-circle.svg" width="128" height="128"></a>

Deep Symmetry&rsquo;s projects are generously sponsored with hosting
by <a href="https://zulip.com">Zulip</a>, an open-source modern team
chat app designed to keep both live and asynchronous conversations
organized. Thanks to them, you can <a
href="https://deep-symmetry.zulipchat.com/#narrow/stream/278354-carabiner">chat
with our community</a>, ask questions, get inspiration, and share your
own ideas.

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

It also offers a [`Message`
class](https://deepsymmetry.org/lib-carabiner/apidocs/org/deepsymmetry/libcarabiner/Message.html)
that you can construct using the response lines that Carabiner sends
you, and it will parse their
[edn](https://github.com/edn-format/edn#edn) structure for you, giving
you a convenient Java String and Map to work with instead.

> If you have any problems or questions, please open an
> [issue](https://github.com/Deep-Symmetry/lib-carabiner/issues) or join
> the conversation on [Carabiner&rsquo;s Zulip
> stream](https://deep-symmetry.zulipchat.com/#narrow/stream/278354-carabiner).


## Licenses

<a href="http://deepsymmetry.org"><img align="right" alt="Deep Symmetry"
 src="doc/assets/DS-logo-github.png" width="250" height="150"></a>

Copyright © 2020-2023 [Deep Symmetry, LLC](http://deepsymmetry.org)

Distributed under the [Eclipse Public License
2.0](https://opensource.org/licenses/EPL-2.0). By using this software
in any fashion, you are agreeing to be bound by the terms of this
license. You must not remove this notice, or any other, from this
software. A copy of the license can be found in
[LICENSE.md](https://github.com/Deep-Symmetry/lib-carabiner/blob/master/LICENSE.md)
within this project.

The included copies of Carabiner are distributed under the [GNU
General Public License, version
2](https://opensource.org/licenses/GPL-2.0). A copy of the license can be found in
[gpl-2.0.md](https://github.com/Deep-Symmetry/lib-carabiner/blob/master/gpl-2.0.md)
within this project.
