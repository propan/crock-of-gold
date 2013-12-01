# crock-of-gold

a demo application to support my talk at ClojureFinland meetup. It's aiming to demonstrate usage of some Clojure template languages and libraries, such as: [hiccup] [1], [enlive] [2], [Selmer] [3] and [clj-jade] [4].

Each section is written using respective library. And all of them are implementing the same functionality. By comparing the sources, you can choose which style of templating you would prefer to use in your project.

If you think a particular implementation can be improved or want to add another example, feel free to create a pull request.

[Live Demo] [5]

## Getting Started

1. Start the application: `lein run-dev` \*
2. Go to [localhost:8080](http://localhost:8080/)

\* `lein run-dev` automatically detects code changes. Alternatively, you can run in production mode with `lein run`.

[1]: https://github.com/weavejester/hiccup
[2]: https://github.com/cgrand/enlive
[3]: https://github.com/yogthos/Selmer
[4]: https://github.com/ryangreenhall/clj-jade
[5]: http://crock-of-gold.herokuapp.com