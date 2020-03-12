# CSRF Demo: Good Site

This site is a simulated banking application used to demonstrate Spring's Cross Site Request Forgery protection.

This application is attacked by [CSRF Demo: Bad Site](http://example.com)

To run the application with CSRF protection disabled, use the following profile:

```
--spring.profiles.active=withoutCsrf
```