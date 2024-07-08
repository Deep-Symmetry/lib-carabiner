# Change Log

All notable changes to this project will be documented in this file.
This change log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased][unreleased]

Nothing so far.


## [1.2.0] - 2024-07-08

### Changed:

- The `details` field in a `Message` is now an `Object` rather than a `Map`, because the
  `version` response sends a `String` and the `unsupported` response sends a `Symbol`.
- Updated embedded Carabiner copies to version 1.2.0, incorporating
  Ableton Link 3.1.2.
- Compile for compatibility back to Java 8, to work with Afterglow (in particular, the
  user guide and API documentation build on Netlify which still uses such an ancient version.)

## [1.1.6] - 2021-02-20

### Changed:

- Updated the macOS carabiner copy to version 1.1.6, adding native
  support for Apple Silicon machines.

## [1.1.5] - 2020-12-28

### Changed:

- Updated embedded Carabiner copies to version 1.1.5, incorporating
  Ableton Link 3.0.3.

## [1.1.4] - 2020-07-01

### Changed:

- Updated embedded Carabiner copies to version 1.1.4.

## [1.1.3] - 2020-02-09

Initial release (version number chosen to match embedded Carabiner
version).


[unreleased]: https://github.com/Deep-Symmetry/lib-carabiner/compare/v1.2.0...HEAD
[1.2.0]: https://github.com/Deep-Symmetry/lib-carabiner/compare/v1.1.6...v1.2.0
[1.1.6]: https://github.com/Deep-Symmetry/lib-carabiner/compare/v1.1.5...v1.1.6
[1.1.5]: https://github.com/Deep-Symmetry/lib-carabiner/compare/v1.1.4...v1.1.5
[1.1.4]: https://github.com/Deep-Symmetry/lib-carabiner/compare/v1.1.3...v1.1.4
[1.1.3]: https://github.com/Deep-Symmetry/lib-carabiner/compare/87f56a3e2a1f8d3822b68214d9ea9da0f3ced839...v1.1.3
