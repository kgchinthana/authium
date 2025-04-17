# Changelog

All notable changes to this project are documented here.

## [Unreleased]
- Additional features and patches in progress.

## [1.0.3] - 2025-04-17
### Changed
- Updated `CHANGELOG.md`, `LICENSE.md`, and `README.md` for cleanup and consistency.

## [1.0.2] - 2025-04-16
### Added
- Docker configuration files.
- Basic environment configuration for local and container-based deployment.

### Changed
- Refactored login endpoint logic.
- Improved authentication and token handling.
- Modified reset password flow and endpoint structure.
- Restructured user service and admin service for clarity.
- Enhanced security logic and replaced RSA with HS256 for JWT.

### Fixed
- Bugs related to admin routes and user role management.
- Issue with `/admin/users/{id}/roles` endpoint.
- Git conflict resolution and broken routes.
- Token refresh issue and token decoding bugs.
- Minor UI structure fixes for reset-password and email components.

## [1.0.1] - 2025-04-04
### Added
- Initial Git and GitHub setup.

## [1.0.0] - 2025-04-04
### Added
- Full source code structure initialized.
- Core features: authentication, authorization, and user role management.
- Utility helpers, unit tests, and complete documentation (`README.md`, `CONTRIBUTING.md`).
- Open-source license.
