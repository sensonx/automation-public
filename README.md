Automation Solution <a href="https://www.linkedin.com/in/senson/"><img src='http://senson.somee.com/art_gif/logo.gif' align='right' width=25 height=25 title='Senson K®'><a/>
===================

Quality Assurance, Test, Preparation and Settings purposes

## How to start?

Install and configure the following applications:
- Environment
  - MySQL (mysql-5.7.18 [winx64], or according to supported by product application)
- Product application (relevant branch & build version)
  - Get the installation source:
    - On created AWS virtual machine (VM), use web-browser AWS S3 URL bookmark (with active credentials)
    - Or by [Dev Jenkins](http://52.26.12.149:18080/userContent/) - select the relevant branch, and the build version
  - Prepare local or remote machine: AIO (*All-In-One*), with access to web application and product application JARs
  - Or prepare storage for relevant product application JARs
- Development tools (DevTool):
  - Required:
    - GitHub (currently: Git-2.16.2-64-bit.exe)
    - Java JDK 1.8
    - IntelliJ (Community - free & open-source)
    - HeidiSQL (MySQL easy GUI client)
  - Optional:
    - Gradle (4 or newest)
    - Notepad++ (latest version)
    - Baretail
    - Remote Machine management tool (e.g. mRemoteNG, Terminals 4.X)
    - AutoIt (the latest version, included 'AutoIt Windows Info' desktop application), only for GUI automation implementation

Prepare SUT file (see '**SUT - System Under Test**' section below) and perform other operations according to '**Manual preparations**' section.

## Automation Solution Structure

The Automation Solution based on the 3 main layers/blocks (see also **Structure Diagram** below):
- **Common** (some call it ADK - Automation Development Kit): the library of a common (independent on the concrete application) services & helpers, like:
  - REST
  - Spring
  - Random
  - Etc.
- **Automation Framework** (FW): the layer supports the concrete application modules and contains two main parts:
  - **Application(s) Services**: functional support (methods), provided by services, e.g.:
    - Internal API Services (REST based)
    - GUI Services (WebApp application(s) side, like Dashboard, Discover, etc. - based on Selenium WebDriver and Page Object Model (POM))
    - Helpers and preparation tools based on DTO (Data Transfer Object)
  - **Data Model**: business and logic architecture of the concrete application modules, presented by a list of DTOs. In most of the cases dictates by GUI side (Web/Mobile solution), which serves end-user (currently, agreed as irrelevant - due to product DTOs usage decision) e.g.:
    - Settings
      - Email
      - LDAP             
      - Etc.
    - Discover
    - Etc.
- **Automatic Tests**, implemented on Auto-Test Gateway base (with an allowed direct connection to FW & Common), included:
  - Auto-Test **Steps Library** supports two main ideas:
    - Serenity BDD report construction
    - Common and FW Façade
  - Auto-Test **Gateway** (TestGW): additional test's common layer, provided common initializations; the main entry point of steps, services & helpers libraries
  - Auto-Test Projects (independent of one another): a collection of projects/modules, where each one contains a list of automatic tests (each test - also independent of one another)

## CI Orchestration Solution

Current CI solution based on Jenkins management, where Jenkins plays the role of orchestrator.
All the tests are managing by TestRail application. It means, that each test (manual or automated) stored in TestRail (as a TestRail Case) and managed by a unique TestRail ID (Case ID). Each implemented automatic test has such a unique TestRail ID, which allows the implementer to get the full test description (details, steps, comments, etc.).

These are the relevant Jenkins jobs (tasks) to provide the current CI processes:
- Prepare environment for testing (AWS machine), included (shortly):
  - Make AWS machine (based on early prepared machine snapshot)
  - Deploy and configure on this machine relevant (branch & build) product application
  - Download and prepare Automation Project (include SUT file preparation)
  - Download test data storage, to be a local
- Execute the selected Automation Test module(s) (includes list of tests)
- Collect all tests execution results and tests IDs (TestRail Case IDs)
- Create TestRail Run report, based on TestRail Case IDs & Run IDs (TestRail API use)
- Create Serenity BDD report, and store it under the web access (possible review and usage)
- Store relevant logs from all sides of the process (application logs, tests logs, etc.)

The TestRail Run provides a common (high-level) module execution Report (included list of the past, failed, ignored, etc. tests), and some tendencies Reports. In most cases these Reports using by managers and provide a high-level view of the results.

The Serenity BDD Report (in addition to the high-level view of the results per module) provides detailed step-by-step information and results (by 'expand/collapse' scenario tree) of each test execution. This allows fast and effective test results review and quickly figure out possible reasons for failure.

![test_rail_jenkins_schema](https://github.com/sensonx/automation-public/blob/main/stuff/readme/TestRail%20-%20Jenkins.jpg)

## SUT - System Under Test

SUT properties file (based on Java supported 'properties' file format) - sets the services to work on the environment (which is under testing), then the services would know - how to login to the various web applications, set web-services clients, execute REST calls and more. This file contains all relevant to the test execution details of the specific tested environment. The file should be updated to point to the relevant applications, DBs, directories, statuses, etc.; check the [example](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/sut/sut.properties). This is a simple and fast way to change or select the relevant environment for the test(s) execution.

## Workplace preparations

- Select the workspace location (VM, PC or laptop) and install there the relevant product version/build
- Prepare [sut.properties](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/sut/sut.properties) file, according to this workspace (see '**SUT - System Under Test**' section also) and the [example](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/sut/sut_example.properties); locate it in some personal place (but - out of Test Automation workspace - e.g. *C:\qa\\<automation_project>*)
- Add new Windows Environment Variable points to **sut.properties** file, by following steps:
  - Open 'System Properties' window
  - Select and open 'Environment Variables' window
  - Add new 'System variable' with a name 'AUTO_TEST_SUT' and value - **sut.properties** file location (path to it's a folder)
  - Update 'Path' parameter by adding %AUTO_TEST_SUT%
  > NOTE: this solution (***system*** *(Windows)* ***environment variable***) allows the correct management of the private **sut.properties** file (located on virtual or real machine), and prevents irrelevant (by mistake) submitting this local **sut.properties** file into GitHub
    >  - Currently, FW supports the following order (priority) of **sut.properties** initialization:
    >>   - [1] Command line execution (JVM properties)
    >>   - [2] Execution by 'system environment variable' value
    >>   - [3] Execution by hard-coded location (e.g. under *..\\<automation_project>*) - as a part of the deployment phase of Jenkins job
- Create ***AutomationData*** folder (according to **sut.properties**, the key is - 'test.data.path', see [example](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/sut/sut_example.properties)), and it's content: find the content as a ZIP file [here](https://drive.google.com/open?id=1Ha06UGM0dOuf_j1SN6oL2g1gpni2NlXv)
  > NOTE: ***AutomationData*** folder contains files, which support most of the tests by customer data storage simulation
- Remember, testing of LDAP connection requires checking allowed access from the workspace to LDAP (according to [ldap.properties](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/properties/ldap.properties))
- Create ***logs*** folder (according to **sut.properties**, the key is - 'test.log.path', see [example](https://github.com/DbGlobe/DaAuthomation/blob/master/stuff/sut/sut_example.properties))
- IRRELEVANT >> Copy following files (JARs) from product location (*..\\<app_name>\\filecluster\lib*) to your Automation project (e.g. *..\\<automation_project>\\auto-fw-services\src\main\resources*):
  - appserver-XX.X.X.jar
  - common-XX.X.X.jar
  - da-media-connector-XX.X.X.jar
  - da-media-connector-impl-XX.X.X.jar
    - XX.X.X = relevant version of JAR
    - Be sure: you use the **same** versions of that JARs (see above) in both locations (product installation & Automation project)
    - This is the temporary solution until will be implemented JAR repository, like [Artifactory](https://www.jfrog.com/artifactory/) or [Nexus](https://www.sonatype.com/nexus-repository-sonatype)
- Register DLLs (required only for GUI automation):
  - Click 'Start' button, type '*cmd*', wait and right-click on 'Command Prompt' item, select 'Run as administrator'
  - In the opened 'Administrator: Command Prompt' window come to the location of the Automation project, like *..\\<automation_project>\\stuff\\dll*
  - If you have OS x64 (and/or Java x64), type and execute following: *regsvr32 **AutoItX3_x64.dll***, close the *success notification* dialog
  - If you have OS x32 (and/or Java x32), make previous line operation for the file ***AutoItX3.dll***

## Make a test

The test implementation and execution based on [Serenity BDD](http://www.thucydides.info/#/) usage.
In two words about: Serenity BDD is an open-source library that allows creating classic test (@Test) and also supports several other known third-party frameworks (like [Cucumber](http://serenity-bdd.info/docs/serenity/#_serenity_with_cucumber) - in this case, it allows creating tests based on the same logic). However, our current automatic test implementation based almost on Serenity BDD **report** functionality only, as **the main** it feature.

So - for this **report** purposes, Serenity BDD provided following 2 main annotations - *@Steps* and *@Step*. First one - is to manage libraries (a separate project of classes like [this](https://github.com/DbGlobe/DaAuthomation/blob/master/facade/auto-facade-steps/src/main/java/com/qa/automation/steps/rest/RestRootFolderSteps.java)) of atomic actions/methods (steps). The second - is to declare that libraries in the [Test](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-sanity/src/test/java/com/qa/automation/sanity/test/RootFolderSanityTest.java) class. So - each @Test (in the Test class) contains several steps (@Step) according to relevant logic/business, which may be divided to known in Cucumber phases: **Given**, **When** and **Then** (not required for our solution and not in use). The libraries (@Steps) itself initialized by TestGW.

Together with all previous, the main platform of the test implementation and execution based on the standard JUnit library (with known attributes, like @Test, @Before, etc.). At the same time, keep in mind that some Serenity BDD solutions - prevent classic JUnit library methods usage (like parameterization) by own overriding.

Minimal Test implementation requirements and steps:
- Understand test scope and requirements, to the next mapping of relevant steps of test execution
- Prepare the Test Plan (Scenario/Flow) of the test, based on the ordered list of relevant steps
- Store the prepared Test Plan in the test manager application (QC, TestRail, etc.), and get the unique ID of this test (in TestRail it will be Case ID) to further usage in Java test implementation
- Review already implemented @Steps (to figure out already implemented functionality)
- Add missed functionality to existing *Steps* classes or implement new *Steps* method(s) and/or class(es)
- Implement Test (and possible - test class)
  - Based on [Test Base](https://github.com/DbGlobe/DaAuthomation/blob/master/facade/auto-test-gateway/src/main/java/com/qa/automation/gateway/base/MainTestBase.java) class
  - Contained steps according to prepared 'simple Test Plan'
  - Store steps list with a short description, located above @Test attribute (each test), in HTML format (serves JavaDoc format)
  - Implement the unique name of the test, according to the following points:
    - Contained the standard prefix with ID (from test manager application), format "*_*< ID >*_*" (see additional **Code and Name Conventions** section), like tests of [this class](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-login/src/test/java/com/qa/automation/login/test/LoginLogoutTest.java)
    - Shortly describe test scenario or purpose (refers to the absence of the need for additional explanations)

## Make a "matrix" test

Sometimes we need to implement several tests when a single difference between maybe value of some parameter (or a number of parameters). The well-known solution is to use the 'matrix' of data. As additional, for prevention of too big code duplication - the best way is to use "parameterization". Due to the usage of Serenity BDD - in our test used *SerenityParameterizedRunner.class* (like [this](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-searchpattern/src/test/java/com/qa/automation/searchpattern/test/matrix/SearchPatternDescriptionMatrixTest.java)), overrides JUnit *Parameterized.class*.
The test class contains several **required** parts:
- *@RunWith(SerenityParameterizedRunner.class)* class attribute
- Scope ID (for automatic generation test names during execution)
- Class constructor (also for automatic generation tests during execution and according to 'matrix' data)
- Private variables, which will be initiated under the constructor
- *@TestData* attributed method, which provides the relevant to us 'matrix' (data = list of values)
- *@Test* method (or several), based that private variables, which were initiated under the constructor

In part of cases, where we're talking about thousands of tests - found additional Serenity BDD Report limitation (inability to create a report). To fix that issue, currently decided to divide such tests classes to several (with smaller 'matrix'). To prevent code duplication (list of @Test methods under each separated class) - decided to store those methods under TestList class. This is a temporary solution, working - but not so cool...

## Make a @Step

According to explained above, implementation of Step based on the following points:
- Functionality going to serve several tests (so - common usage)
- Wrap the Service (Helper, Library) usage by entered into report information (e.g. stepInfoReport(...) method)
- Example of relevant information:
  - Report input parameters
  - Report output values
  - Report service(s) steps details (parameters, values, exceptions, etc.)
  - In case of REST usage, report URL and method
  - In case of GUI usage, report operation(s) details
  - In case of parameter validation, report results
- Use the following simple rules to information presentation:
  - Provide parameter value as the @Step description ONLY if method has:
    - Single parameter
    - This single parameter is a simple Object (Enum, String, Long, etc.) or primitive
    - This single parameter is not DTO
    - This method has no overloading (Serenity BDD known issue)
  - Provide parameter value by report steps (inside the method body:
    - As single parameter and a simple Object (Enum, String, Long, etc.) or primitive - in the same line: **[%s]**
    - Else (DTO, List, etc.) - in the new line: **\n%s**
- Implement parameters (Objects and DTOs) validation (e.g. parameter not empty, not null, etc.), which can interrupt and stop the test execution
- Set FailRank values

## Code and Name Conventions

The base for all implemented code - is common Java Code Conventions (e.g. packages naming, code formatting, etc.).
In addition - please follow to the next simple points (to make code simple, common, and easy to support & maintenance):
- Each test class has the suffix *Test*, like [*RoleSanity**Test***](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-sanity/src/test/java/com/qa/automation/sanity/test/RoleSanityTest.java); or suffix *GuiTest* for tests based on Selenium WD usage, like [*DataRoleSanity**GuiTest***](https://github.com/DbGlobe/DaAuthomation/blob/master/test-ui/qa-ui-test-role/src/test/java/com/qa/automation/ui/role/test/DataRoleSanityGuiTest.java)
- Each @Test method start with the prefix *test*, like [***test**DeleteAddedRole()*](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-sanity/src/test/java/com/qa/automation/sanity/test/RoleSanityTest.java); or with the prefix *testGui_* for tests based on Selenium WD usage, like [***testGui**_CreateNewDataRoleBasedNewRoleTemplate()*](https://github.com/DbGlobe/DaAuthomation/blob/master/test-ui/qa-ui-test-role/src/test/java/com/qa/automation/ui/role/test/DataRoleSanityGuiTest.java)
- In case the Automation Solution includes connection to a Tests Manager Application (like QC, TestRail, etc.) - each @Test method name prefix contains also test ID from such application (as a unique connection between @Test and Tests Manager), like [*test_**61650**_VerifyLogout()*](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-login/src/test/java/com/qa/automation/login/test/LoginLogoutTest.java)
- Each test class inherits from [MainTestBase.class](https://github.com/DbGlobe/DaAuthomation/blob/master/facade/auto-test-gateway/src/main/java/com/qa/automation/gateway/base/MainTestBase.java), which provide almost common initializations, preparations, and cleaning (or - inherits from the local base class, see below)
- When the test implementation based on the local base class - such base class should use suffix *TestBase*, like [*SystemRole**TestBase***]( https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-role/src/test/java/com/qa/automation/role/base/SystemRoleTestBase.java) and should inherit from **MainTestBase.class**
- Each @Test method has @Title attribute with test scope tag (like "RootFolder Sanity: ") and detailed test title to support readable Serenity BDD report (see [*@Test testDeleteAddedRole()*](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-sanity/src/test/java/com/qa/automation/sanity/test/RoleSanityTest.java))
  > NOTE: due to some limitations of Serenity Report (HTML based) and limitations of Jenkins Report preparation (DevOps side), the following characters are **forbidden** to use:
  >- Jenkins Report limitations: **, '** (in the test name (@Title) and assertion messages)
  >- Jenkins Email-Report limitations: **| , ( ) &** (in the test name (@Title))
  >- Serenity Report limitations: **| &** (in the test name (@Title), step description, any messages: assertion, error, info, etc.)
- The main vision that the test class contains only @Test methods, all other private & help methods should be managed under Base or [Helper](https://github.com/DbGlobe/DaAuthomation/blob/master/adk/auto-fw-services/src/main/java/com/qa/automation/help/RoleHelper.java) classes (like [LdapSanityTest.class](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-ldap/src/test/java/com/qa/automation/ldap/test/LdapSanityTest.java) and its [LdapTestBase.class](https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-ldap/src/test/java/com/qa/automation/ldap/base/LdapTestBase.java))
- As noted early, the main style for test class implementation - it's "test class contains only @Test methods". This construction based on the following main requirements: code duplication prevention and implemented code reuse. However, in many cases, we have the situation, where common (for several tests) code (e.g. method, enum, etc.) is too much specific for current scope, and the better solution is to manage such code under the current test class. This is can be a list of common steps, executed according to some flag (see [this example](
  https://github.com/DbGlobe/DaAuthomation/blob/master/test-api/qa-test-role/src/test/java/com/qa/automation/role/test/matrix/systemrole/SystemRoleMatrix01Test.java)...
- As additional to previous points, the main vision also that the test class contains only @Step methods: means - no service usage on a test level. All the services (and mostly helpers and utilities) use in test code body wrapped by @Step (from steps library). Such implementation allows using the steps library as Façade vs. FW services, helpers and utilities. Besides, that allows creating a standard, informative and user-friendly report (supported by Serenity BDD)
- The next several points required to support **Fail-Rank** side of tests executions report:
  - Each @Step usage on test level should be wrapped by **assert** (from Common Base classes) if that’s allowed
  - If 'no' (see prev.point), **assert** should wrap the variable, which is the result of @Step usage
  -	For this requirement (see both above), each @Step usage (test/step level) should be separated into single actions
  -	When a test uses the internal private method:
    - [1] The long method with internal assert actions, same to 1-st point (see above)
    - [2] Long and Short (without internal assert actions) method should return the boolean parameter, which will be wrapped by assert (with an informative message) in the test body
  -	No internal private method as 'void' method
  -	If according to test business/flow is necessary to set specific Fail-Rank value (different from provided by @Steps) – do this!
  -	Each @Step should verify all input parameters, with a relevant message in exception case (before that parameters usage)
  -	Each @Step should verify all result values, with a relevant message in exception case (before return)
  -	Each @Step should provide Fail-Rank as default (first line in the body) and according to input parameters validation (LOW)
  -	No @Step as 'void' method (except 'Cleanup' steps)
- Be sure, that the final submitted (to source control storage, e.g. GitHub) test contains description and/or execution steps (include validations), written according to JavaDoc requirements (see already implemented tests)
- As noted early (again), this solution based on Serenity BDD usage. This 'third party FW' provides some pros and cons. One of the serious minuses - that Serenity BDD does not fully support overloading (classic OOP). For creating different methods with the same name (overloading), use unique @Step annotation and simple parameters values should be presented in the method body by report step usage: like [getDataCenter(String name) and getDataCenter(Long id)](https://github.com/DbGlobe/DaAuthomation/blob/master/facade/auto-facade-steps/src/main/java/com/qa/automation/steps/rest/RestDataCenterSteps.java)
- Each time, when need to use product/application/system or any 'saved' term - check if the product (Java side) provides relevant enum, else - make such enum to your local usage (next time such enums will be reviewed to possible moving into FW layers)

## Exception Handling

Currently, Steps library almost not provides steps to Exception Handling. The single example [is here](https://github.com/DbGlobe/DaAuthomation/blob/master/facade/auto-facade-steps/src/main/java/com/qa/automation/steps/rest/RestLoginSteps.java).
In the next future will going to do this in more tests, and should remember each such @Step – use in name term "Try" and should verify GUI or REST exception (text).

## Tips & Tricks (test run, gradle, etc.)

- Enum vs. "Null Pointer Exception" - **no need** it prevention for string presentation in any place (report, console, etc.):
  - Logger (org.apache.logging.log4j.Logger)
  - Logger (org.slf4j.Logger)
  - String
  - String.format(...)
  - StringBuilder
  - StringHelper.toString(...)
- When the test failed, application logs (*..\\<app_name>\\filecluster\logs*) stored as a ZIP file, according to the location managed by *sut.properties* ('test.log.path')
- Also (when the test failed) the test log (console output) stored as a file, in the same location managed by *sut.properties* ('test.log.path')
- Remember, Serenity BDD allows execution task 'aggregate' to prepare local report, contains executed test or tests
- Recommended execute 'Build Project' command each time you've updated JARs to new (to sync IntelliJ indexes)
- Clean the JARs cache (IntelliJ, Gradle, etc.) - find files under *"C:\Users\ {account}\ .gradle"*
- Switch Git branch (*cmd line, under automation project directory): *git checkout -b <**branch_name**> origin/<**branch_name**>*
- Gradle build (IntelliJ command line): *clean build -x test -x checkOutcomes*
- Gradle test module execution (CMD command line):  *gradlew test aggregate -i -p {test_module_path}* (e.g. *D:\qa\\<automation-project>\\<test-module>*)
- Execute build with JAR creation (IntelliJ): *File* > *Project Structure* > *Project Settings* > *Artifacts* > **[+]** 'plus' sign > *JAR* > *From modules with dependencies...*
- Set parameters in "*..\\<app_name>\\mediaProcessor\config\application.properties*":
    - Activate Preserve Accessed Time: **scan.file-share.preserve-accessed-time.active=true**
- Set parameters in "*..\\<app_name>\\filecluster\config\application.properties*":
    - QA REST API: **qa-api-enabled=true**
    - LDAP: **ldap.user-tokens.add-domain-prefix.map=mytestingda.com:CORP,testing.da.com:TESTING**
    - REST call traces:
        - Get and review: **management.endpoints.web.exposure.include=httptrace**
        - Use [Actuator-HTTP-Trace](http://localhost:8080/actuator/httptrace) to review last 100 HTTP calls
- IntelliJ configurations:
    - Go to *File > Settings > Editor > File Encoding*, set:
        - "Global Encoding": UTF-8
        - "Project Encoding": UTF-8
        - "Default encoding for properties files": UTF-8
        - Under "Path" list add path to main (root) automation project
    - Go to *File > Project Structure > Project*, select Java 8, e.g. "C:\Program Files\Java\jdk1.8.0_221"
    - Go to *File > Settings > Build, Execution, Deployment > Build Tools > Gradle*, choose in 'Gradle JVM' - "Use Project JDK"
- Lombok
    - Use [Lombok](https://projectlombok.org/features/all) features (to FW DTOs)
    - IntelliJ simple configuration (if needed): come to *Settings > Build, Execution, Development > Compiler > Annotation Process*,  select check-box 'Enable annotation processing'
- Upgrade to Java 11:
  - Download from *<GoogleDrive>\Share\Engineering\environment\3rd-party* - **solr-7.7.2.zip** and unzip it to the cla-filecluster folder
  -	Kill all Java processes from the Tasks Manager
  - Download [Java 11](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase11-5116896.html) and install
  - IntelliJ side:
    - Go to *File > Project Structure > Project*: press 'New' add e.g. "C:\Program Files\Java\jdk-11.0.3"  
    - Go to *File > Settings > Build, Execution, Deployment > Gradle*: choose in 'Gradle JVM' - "Use Project JDK" 
    - Go to *File > Settings > Build, Execution, Deployment > Compiler -> Java Compiler*: set 'Project bytecode version' - "11", in 'Javac options' select checkbox "Use compiler from module target jdk  when possible"
    - Go to *File > Invalidate Caches / Restart...*: choose 'Invalidate and Restart'
    - Go to 'Gradle' tab: press refresh
    - Go to 'Build': rebuild
    - gradle.build file add following dependencies: (TBD)

## Code Management (vs. GitHub)

Those are main rules to correct Code management (vs. GitHub, branches, etc.)
- Before every submit (push) operation (to *your* individual *remote branch*, e.g. **sprint-xyz-*username***):
  -	Take the latest code from *main remote branch* (e.g. – **sprint-xyz**) to your local source (sync by 'pull' operation)
  -	If the sync is failed, check which files were changed vs. latest *main remote branch* version
       - If change is relevant - perform additional sync vs. special file
       - If change is irrelevant - perform 'rollback' operation for this special file and repeat the whole sync
  -	Make *local branch* build, check it’s PASS
  -	If the build failed, update relevant points, vs. FW & Façade and make *local branch* build, check it’s PASS
  -	Push your (synced) code to *your remote branch*
  -	Open task to code review & merge into a *main remote branch*
- Each time you’re starting to work with part of the code, check you have an *individual ‘code access rights’* on it:
  -	If it’s a FW code – it’s CRITICAL
  -	If it’s a Façade code – it’s IMPORTANT
  -	If it’s a Test code – it’s GOOD TO HAVE
  -	Individual ‘code access rights’ = during the time you're on this part of the code, no one more has no deal with this part
- If you’ve not finished working with part of the code, but others need its access:
  -	Agree about the possible format to save your updates (remark, fast sync, copy/paste, etc.)
  -	Submit your updates (if agreed), according to an agreed way
- TALK to your colleague!!! Any updates (which probably may affect other code writers) should be provided to all relevant sides as soon as possible, in every effective way:
  -	Face to face
  -	Email, Slack (or another Web/Mobile messenger), SMS, etc.
  -	JIRA

## Automation Solution Structure Diagram

![automation structure diagram_](https://github.com/sensonx/automation-public/blob/main/stuff/readme/ADK%20-%20Senson%20K%C2%AE.jpg)

## Application and Product Solution Diagram

![da application structure diagram_](https://github.com/sensonx/automation-public/blob/main/stuff/readme/DA_Schema.jpg)

## Agreed terminology (comments / code usage)
| Comment / Code Usage Term | Product DTO | Note |
| ------------------------- | ------ | ---- |
| BusinessList | BizListDto |  |
| BusinessListItem | SimpleBizListItemDto |  |
| DataCenter | CustomerDataCenterDto |  |
| DataRole | FunctionalRoleDto |  |
| DocFolder | DocFolderDto | Physical folder |
| DocType | DocTypeDto |  |
| File | ClaFileDto | Physical file |
| FileGroup | GroupDto | Analysis Group (item created by system only) |
| MediaConnection Details | MediaConnectionDetailsDto |  |
| MediaProcessor | ClaComponentDto | One of possible CLA Components |
| MS-ExchangeConnection Details | Exchange365ConnectionDetailsDto |  |
| OneDriveConnection Details | OneDriveConnectionDetailsDto |  |
| OperationalRole | BizRoleDto |  |
| PatternCategory | PatternCategoryDto |  |
| RefinementRule | RefinementRuleDto |  |
| Regulation | RegulationDto |  |
| RoleTemplate | RoleTemplateDto |  |
| RootFolder | RootFolderDto | Virtual folder |
| ScheduleGroup | ScheduleGroupDto |  |
| SearchPattern | TextSearchPatternDto |  |
| SharePointConnection Details | SharePointConnectionDetailsDto |  |
| SystemRole | SystemRoleDto |  |
| SystemUser | UserDto |  |
| SubFileGroup | GroupDto | Sub-FileGroup (item created manually by RefinementRule usage) |
| Tag | FileTagDto | Supports Files, DocFolders and FileGroups |
| TagType | FileTagTypeDto | Supports Files, DocFolders and FileGroups |
| WorkGroup | WorkGroupDto |  |
| WorkGroupsScope | WorkGroupsScopeDto |  |
| UserFileGroup | GroupDto | User FileGroup (item created manually by join several FileGroups) |
| UserScope | UserScopeDto |  |

## Tests scope (by modules, relevant to Jenkins jobs)
- Alert
  - File operations
  - Folder operations
- Association
  - DocType with Tags Matrix
    - Son / Parent
    - Created / Predefined DocType
    - Created / Predefined TagType/Tag
  - DocFolder Matrix
    - By: BusinessListItem, DataRole, DocType, Tag    
  - FileGroup Matrix
    - By: BusinessListItem, DataRole, DocType, Tag    
- BusinessList
  - Association
    - Add (DocFolder, FileGroup, File)
    - Remove (DocFolder, FileGroup, File)
    - Priority (File vs. SubDocFolder vs. RootFolder, File vs. FileGroup)
    - FileTypes (MS-Word, MS-Excel, PDF, etc.)
  - Extract
  - Matrix
    - Name
    - Description
    - Item State
    - Item Type
- Business (Flows)
  - Action Engine
  - DataRole Assignment
  - Tag Assignment
  - FileGroups Join
  - Filter By Non-Existent Item
  - Scan Map Only
  - Scan Not Full
  - Rescan
  - Scan & EML files
  - Search Patterns Regular & Postal
- BVT
  - Action Execution Engine
  - Assign and ‘Apply to Solr’: Tag, DocType, DataRole
  - BusinessList Assignment
  - DocType
  - Rescan RootFolder
  - RootFolder
  - SystemUser
  - TagType and Tag
- DataCenter
  - Basic (sanity)
  - Negative
  - Matrix
    - Name
    - Description
    - Location
- Department
  - Scan (Phases, Map/Ingest/Analyze Only)
  - Settings (CRUD & Clone)
  - RootFolder (Create/Update/Negative)
  - DocFolder (Negative)
  - Association (RootFolder, DocFolder, SubFolders)
  - Matrix
    - Name
    - Description
    - Path
    - UniqueName
    - DocFolder assignment (assign, re-assign, remove)
- DocType
  - Assign
  - Rescan
  - Filter
  - Negative
  - Tag Assignment
  - Matrix
    - Name
    - Description
    - Alt Name
    - Unique Name
    - Update
    - Predefined
    - Association Priority (File vs. SubDocFolder vs. RootFolder, File vs. FileGroup)
        - Son / Parent
        - Created / Predefined
    - Association FileTypes (MS-Word, MS-Excel, PDF, etc.)
        - Son / Parent
        - Created / Predefined
- E2E
  - FileGroup Highlight Search
  - FileGroup with DocTypes Join
  - FileGroup with Files Join
  - FileGroup with Tags Join
  - RootFolder Scan
- Export
  - Audit
  - Discover
  - Ingestion Errors
  - Scan Errors
- File
  - Email as File
  - File without Tags
- FileGroup
  - Files inside
  - Join
  - Name and revert
  - Attach & Detach
  - FileGroup Matrix
    - Name (update)
  - RefinementRule
    - Sanity
    - Multiple Rules
    - Negative
    - Matrix
      - Name
      - Predicate
      - Priority
- LDAP
  - Sanity
  - Negative
  - User with OperationalRole (matrix)
  - User with SystemRole (matrix)
- Login
  - Login & Logout
- MediaConnection
  - MS-Exchange
    - Basic
    - Negative
    - Multiple EmailGroup
    - Single EmailGroup
    - RootFolder with Multiple EmailGroup
    - RootFolder with Single EmailGroup
    - Matrix
      - Name
      - Application ID
      - Tenant ID
      - Password
  - SharePoint
    - Matrix
      - Name
      - Domain
      - URL
      - Username
- Role
  - DataRole
    - Add (DocFolder, FileGroup, File)
    - Remove (DocFolder, FileGroup, File)
    - Priority (File vs. SubDocFolder vs. RootFolder, File vs. FileGroup)
    - Priority Multiple (File vs. SubDocFolder vs. RootFolder, File vs. FileGroup)
    - FileTypes (MS-Word, MS-Excel, PDF, etc.)
  - DataRole Matrix
    - Name
    - Description
  - OperationalRole Matrix
    - Name
    - Description
  - SystemRole Matrix
    - Basic
- Role – DataRole
  - Matrix
    - Negative
    - Set
    - Single
    - Update
- Role – OperationalRole
  - Predefined Access
  - Matrix
    - Negative
    - Set
    - Single
    - Update
- RootFolder
  - Create
  - Delete
  - Matrix
    - Name
    - Path FileShare
    - Path SharePoint
    - EmailGroup
- Sanity
  - BusinessList
  - DataCenter
  - DocType
  - File
  - Role
  - RootFolder
  - Scan RootFolder
  - Scan
  - ScheduleGroup
  - Tag
  - SystemUser
- Scan
  - Scan RootFolder
  - Rescan RootFolder
  - Scan RootFolder by JobType
  - Scan RootFolder command (Pause, Stop, Resume…)
  - Scan RootFolder on JobType
  - Scan RootFolder until JobType
  - Scan ScheduleGroup by Job Type
  - Scan ScheduleGroup command (Pause, Stop, Resume…)
  - Scan ZIP based RootFolder
- ScheduleGroup
  - Matrix
    - Name
    - Description
    - Negative
    - Negative Weekly Set
    - CRUD
      - Hourly
      - Daily
      - Weekly
- Scope
  - UserScope Sanity
  - WorkGroupScope Sanity
  - UserScope & WorkGroupScope vs. DocType
  - UserScope & WorkGroupScope vs. Tag & TagType
  - WorkGroupScope
    - WorkGroup Sanity
    - WorkGroup and User
    - Discover Item Access
- SearchPattern
  - Sanity
  - Tag
  - Matrix
    - Name
    - Description
    - Status
    - Type
- TagType
  - Basic
  - Filter
  - Rescan
  - Matrix
    - Tag Name
    - Tag Description
    - Tag Same Name
    - TagType Name
    - TagType Description
    - TagType Same Name
    - Tag vs. Predefined TagType
    - Tag Association Priority (File vs. SubDocFolder vs. RootFolder, File vs. FileGroup)
        - Single / Multiple
        - Created / Predefined
    - Tag Association FileTypes (MS-Word, MS-Excel, PDF, etc.)
        - Single / Multiple
        - Created / Predefined
- User (SystemUser)
  - Matrix
    - Predefined RoleTemplate
    - Name
    - Username
    - SystemRole
    - Full Set
- Workflow
  - Sanity
  - Basic
  - File Data
  - Filter
  - Negative
  - Matrix
    - State & Priority
- Audit (TestZone…)
  - Parameters
  - Product Change
  - Records Length Verification
  - Records Scope Verification
  - RootFolder
