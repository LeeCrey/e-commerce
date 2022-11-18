# Note

Don't get confused with what do i mean by sessions, unlock, passwords and registrations repository.
They are designed to meet Ruby on Rails devise authentication gem(library);

In devise gem(library), there are 5 controllers. They are sessions_controller,
registrations_controller, unlock_controller, and passwords_controller.

I wanted to use only 4 controllers because they do what i want to do.

## Sessions controller

=> handles login and logout method.

## Registrations controller

=> handles sign up, account edit and delete account.

## Unlock controller

=> handles requesting account unlock instruction, and unlocking locked account.

## Passwords controller

=> handles requesting account reset instruction, and changing password.
