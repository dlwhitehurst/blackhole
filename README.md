# blackhole

[![Build status](https://travis-ci.org/dlwhitehurst/blackhole.svg?branch=master)](https://travis-ci.org/dlwhitehurst/blackhole)

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).
With two goals in mind, I started Blackhole to learn AngularJS and the modern-side of web development. And, I needed an application to keep
records for my consulting business. Blackhole provides a lot of functionality and a dead-simple double-entry accounting system that includes
reports. I only support US currency now, but I have plans to internationalize the entire application and allow the customization of any default
currency.

Currently, the application has a general journal that writes to the ledger accounts when the journal entry is created. General ledger features
are provided so you can adjust entries or correct mistakes. Once the journal entry system has been fully tested, the ledger account features will
be modified more to suit the retrieval than updates. If you wish to view only the "Cash" account, you can enter 100 or the chart identifier in the
search text box of the ledger entries page and see only the cash account. Select the posting ref column header and the table will support by journal
entry and posting order of the ledger account. A chart of accounts is created by default, but they can be changed or customized. At any time you can
select Reports/Trial Balance to check your work. The other reports are currently being created now.

Blackhole was written for me but you can download the source at the Github link below. It may suit your needs as a traveling entrepreneur, independent
consultant, or small-business.

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.


## Building for production

To optimize the blackhole client for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript/` and can be run with:

    gulp test



## Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `blackhole`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/blackhole.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/
