# Core library for clean architecture in Java

## Introduction

This documentation guides you through the utilization of the core library for implementing clean architecture in project
using Java.

We'll explore the creation of custom application request and use cases, paying special attention to handling missing and
unauthorized fields.

Practical examples are provided using code snippets from a test suite to showcase the library's usage in building a
modular and clean Java application.

## Prerequisites

Ensure that you have the following:

- `Java` installed on your machine (version `>=21`).

## Installation

```
<dependency>
    <groupId>com.ug</groupId>
    <artifactId>clean-architecture-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Core Overview

### Application Request

Requests serve as input object, encapsulating data from your http controller. In the core library, use the `com.ug.request.Request` class
as the foundation for creating custom application request object and implements `com.ug.request.RequestInterface` interface.
Define the expected fields using the `getRequestPossibleFields()` methods.

### Presenter

Presenters handle the output logic of your usecase. You have to extends `com.ug.presenter.Presenter` and
implements `com.ug.presenter.PresenterInterface` interface.

### Usecase

Use cases encapsulate business logic and orchestrate the flow of data between requests, entities, and presenters.
Extends the `com.ug.usecase.Usecase` class and implements `com.ug.usecase.UsecaseInterface` with the execute method.

### Response

- Use `com.ug.response.Response` to create usecase `response`.
- Supports success/failure status, custom message, HTTP status codes, and response data.
- I recommend you to extends `com.ug.response.Response` class to create your own response


## Example of how to use the core library

> NB: I recommend you to @see all tests in `tests` folder to get more about examples.

1. Creating a custom request and handling missing/unauthorized fields

- Extends `com.ug.request.Request` and implements `com.ug.request.RequestInterface` to create
  custom application request objects.
- Define the possible fields in the `getRequestPossibleFields` methods.

```java
import com.ug.request.RequestInterface;
import com.ug.request.Request;
import com.ug.exception.BadRequestContentException;

import java.util.Map;

interface CustomRequestInterface extends RequestInterface {
}

class CustomRequest extends Request implements CustomRequestInterface {
  @Override
  public Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "firstname", true,
      "lastname", true,
      "roles", true
    );
  }
}

// You can also apply constraints to the request fields. For that you have to modify the `applyConstraintsOnRequestFields` methods as below:
class CustomRequest extends Request implements CustomRequestInterface {
  protected void applyConstraintsOnRequestFields(Map<String, Object> requestData) {
    // your validation logic here
  }
}

// when unauthorized fields
class CustomRequest extends Request {
  public Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "field_1", true,
      "field_2", true
    );
  }
}

RequestInterface customRequest = new CustomRequest();
try {
  RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
    "field_1", true,
    "field_2", true,
    "field_3", 1
  ));
} catch (BadRequestContentException error) {
  Map<String, Object> errorDetails = error.format();
  
  assertEquals(Status.ERROR.getValue(),errorDetails.
  
  get("status"));
  
  assertEquals(StatusCode.BAD_REQUEST.getValue(),errorDetails.
  
  get("error_code"));
  
  assertEquals("illegal.fields",errorDetails.get("message"));
  
  assertEquals(Map.of("unrequired_fields", List.of("field_3")),errorDetails.
  
  get("details"));
}

// when missing fields
class CustomRequest extends Request {
  public Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "field_1", true,
      "field_2", Map.of("field_3", true)
    );
  }
}

RequestInterface customRequest = new CustomRequest();
try{
  RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
    "field_1", true,
    "field_2", 1
  ));
  }catch(BadRequestContentException error){
  Map<String, Object> errorDetails = error.format();
  
  assertEquals(Status.ERROR.getValue(),errorDetails.
  
  get("status"));
  
  assertEquals(StatusCode.BAD_REQUEST.getValue(),errorDetails.
  
  get("error_code"));
  
  assertEquals("missing.required.fields",errorDetails.get("message"));
  
  assertEquals(Map.of("missing_fields", Map.of("field_2", "required field type not matching array")),errorDetails.
  
  get("details"));
}

// when everything is good
class CustomRequest extends Request {
  public Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "field_1", true,
      "field_2", true
    );
  }
}

Map<String, Object> payload = Map.of(
  "field_1", new String[]{"yes", "no"},
  "field_2", 3
);

RequestInterface customRequest = new CustomRequest();
RequestInterface instanceRequest = customRequest.createFromPayload(payload);

assertEquals(payload.get("field_1"),instanceRequest.

toArray().

get("field_1"));

assertEquals(payload.get("field_2"),instanceRequest.

toArray().

get("field_2"));

assertNotNull(instanceRequest.getRequestId());  // 6d326314-f527-483c-80df-7c157acdb95b


// or with nested request parameters
class CustomRequest extends Request {
  @Override
  public Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "field_1", true,
      "field_2", Map.of(
              "field_3", true
      )
    );
  }
}

RequestInterface customRequest = new CustomRequest();
RequestInterface instanceRequest = customRequest.createFromPayload(Map.of(
  "field_1", true,
  "field_2", Map.of(
          "field_3", 4
  )
));

assertTrue(instanceRequest instanceof CustomRequest);
```

2. Create the custom presenter

- Extends `com.ug.Presenter` to create `presenters`.

```java
import com.ug.presenter.Presenter;
import com.ug.presenter.PresenterInterface;

interface CustomPresenterInterface extends PresenterInterface {
}

class CustomPresenter extends Presenter implements CustomPresenterInterface {
  // you can override parent methods here to customize them
}
```

3. Creating a custom usecase and execute it

- Extends `com.ug.usecase.Usecase` and implements `com.ug.usecase.UsecaseInterface` to create your use cases.
- Implement the `execute` method for the use case logic.

```java

import com.ug.usecase.Usecase;

class CustomUsecase extends Usecase {
  @Override
  public void execute() {
    this.presenter.present(
      Response.create(
        true,
        StatusCode.NO_CONTENT.getValue(),
        "success.response",
        Map.of(
          "field_1", "yes",
          "field_2", new HashMap<>()
        )
      )
    );
  }
}

CustomUsecase instanceUsecase = new CustomUsecase();
instanceUsecase.withPresenter(instancePresenter).execute();

ResponseInterface response = instancePresenter.getResponse();

assertNotNull(response);

assertTrue(response instanceof Response);

assertTrue(response.isSuccess());

assertEquals("success.response",response.getMessage());

assertEquals(StatusCode.NO_CONTENT.getValue(),response.getStatusCode());

assertEquals(Map.of("field_1", "yes","field_2",new HashMap<>()),response.getData());

assertEquals("yes",response.get("field_1"));

assertNull(response.get("field_3"));

assertEquals(
    Map.of(
      "status", Status.SUCCESS.getValue(),
      "code",StatusCode.NO_CONTENT.getValue(),
      "message","success.response",
      "data",
      Map.of("field_1","yes","field_2",new HashMap<>())),instancePresenter.getFormattedResponse()
);


// without request and presenter
class CustomUsecase extends Usecase {
  @Override
  public void execute() throws BadRequestContentException {
    throw new BadRequestContentException(
      new HashMap<String, Object>() {{
        put("message", "BadRequestContentError");
        put("details", Map.of("field_1", "yes"));
      }}
    );
  }
}

CustomUsecase instanceUsecase = new CustomUsecase();
try{
  instanceUsecase.execute();
}catch(
BaseException error){
Map<String, Object> errorDetails = error.format();

assertEquals(Status.ERROR.getValue(),errorDetails.get("status"));

assertEquals(StatusCode.BAD_REQUEST.getValue(),errorDetails.get("error_code"));

assertEquals("BadRequestContentError",errorDetails.get("message"));

assertEquals(Map.of("field_1", "yes"),errorDetails.get("details"));
}

// with request and presenter
class CustomUsecase extends Usecase {
  @Override
  public void execute() {
    this.presenter.present(
      Response.create(true, StatusCode.OK.getValue(), "success.response", this.getRequestData())
    );
  }
}

class CustomRequest extends Request {
  @Override
  protected Map<String, Object> getRequestPossibleFields() {
    return Map.of(
      "field_1", true,
      "field_2", true
    );
  }
}

Map<String, Object> payload = Map.of(
  "field_1", true,
  "field_2", 3
);

CustomUsecase instanceUsecase = new CustomUsecase();
instanceUsecase
  .withRequest(new CustomRequest())
  .createFromPayload(payload)
  .withPresenter(instancePresenter)
  .execute();

ResponseInterface response = instancePresenter.getResponse();

assertNotNull(response);

assertTrue(response instanceof Response);

assertTrue(response.isSuccess());

assertEquals("success.response",response.getMessage());

assertEquals(StatusCode.OK.getValue(),response.

getStatusCode());

assertEquals(payload, response.getData());

assertEquals(true,response.get("field_1"));

assertEquals(3,response.get("field_2"));

assertEquals(
  Map.of(
    "status", Status.SUCCESS.getValue(),
    "code",StatusCode.OK.getValue(),
    "message","success.response",
    "data",payload
  ),instancePresenter.getFormattedResponse()
);
```

4. When error throwing

When errors throwing, you can use some methods.

```java

// for exception, some method are available
exception.getErrors(); // print details
{
  details: {
      field_1: 'required'
  }
}

// or
{
  details: {
      error: 'field [username] is missing.'
  }
}

exception.getDetails(); // print error details
{
  field_1: 'required'
}


// or 

{
  error: 'field [username] is missing.'
}

exception.getMessage() // 'error.message'
exception.getDetailsMessage() // 'field [username] is missing.' only if error key is defined in details.


exception.format();
{
    status: 'success or error',
    error_code: 400,
    message: 'throw.error',
    details: {
        field_1: 'required'
    }
}
```


## Units Tests

You also can execute unit tests.

```
$ yarn test
```

## License

- Written and copyrighted ©2023-present by Ulrich Geraud AHOGLA. <iamcleancoder@gmail.com>
- Clean architecture core is open-sourced software licensed under the [MIT license](http://www.opensource.org/licenses/mit-license.php)