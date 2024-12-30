# Product Distribution in a Supermarket

We have a supermarket, and we want to find the optimal distribution of products to maximize customer purchases. To achieve this, we will assume that the probability of a customer buying a product increases if that product is placed next to a related product (e.g., if someone is buying beer and sees chips nearby, they are more likely to remember to buy them). We also assume that every pair of products has a degree of similarity or relationship, which the user knows and can provide.

To simplify, we will assume that the supermarket only has a circular shelf with a single tier for placing products (or multiple tiers within this circular shelf). Based on the similarity between the products the user wants to offer, the system will determine the optimal distribution of products to maximize the probability of customer purchases.

## Minimum Required Functionalities

To ensure the project is approved, the system must at least provide the following functionalities:

- **Product Management**
- **Management of Similarities Between Products**
- **Distribution Calculation**
  - The program must implement at least two algorithms to find the optimal distribution:
    - A basic solution (e.g., brute force or greedy algorithm)
    - An approximation algorithm (further details will be provided)
  - Any parameters for these algorithms, if applicable, must be interactively configurable within the application.
  - Additional optimizations to both solutions will be valued.
- **Post-Modification of Proposed Solutions**
- **Data Input Options**
  - The data must be definable via the program or importable from a text file.

## Optional Features

Teams may extend the system with additional functionalities, such as:

- Using constraints in the product distribution
- Implementing additional algorithms
- Other innovative extensions

## Evaluation Criteria

In addition to standard quality factors for any program (design, coding, reusability, modifiability, usability, documentation, etc.), the efficiency and flexibility of the system will be particularly valued.





## Useful commands (Gradle):

- `./gradlew test`: will run your unit tests.
- `./gradlew run`: will run your application in the environment. This is useful to test your application in the development environment.
- `./gradlew jar`: will create the jar inside the directory `<project root>/build/libs` with only the project's code. Not dependencies.
- `./gradlew assembleDist`: will create a `.tar` and a `.zip` (both contain the same) in the directory `<project root>/build/distributions` that contain the whole directory structure that will allow to install your project along with its dependencies in a machine without IDE (only with Java 11 installed) and run it.
- `./gradlew clean`: will clean the compilation files and the created artifacts.

More info in the gradle documentation: 
https://docs.gradle.org/current/userguide/application_plugin.html_

