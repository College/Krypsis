import org.krypsis.http.request.Request
import org.krypsis.http.response.SuccessResponse
import org.krypsis.http.response.ErrorResponse
import org.krypsis.http.response.ErrorResponse
import org.krypsis.http.response.SuccessResponse
import org.krypsis.http.request.Request
import org.krypsis.http.request.Request
import org.krypsis.http.request.Request

scenario "an error with an message", {

  given "the request and response", {
    url = "${Request.NAMESPACE}base/getmetadata"
    message = "command 'foo' is not supported"
    request = new Request(url);
    error = new ErrorResponse(request, ErrorResponse.COMMAND_NOT_SUPPORTED, message)
  }

  when "the to js function is executed", {
    result = error.toJavascriptFunction()
  }

  then "the result must match the expected string", {
    result.contains('Krysis.Device.Base.Error.getmetadata')
  }

  and "the parameter code an message are present", {
    result.contains("code: ${ErrorResponse.UNKNOWN}")
    result.contains("message: '${message}'")
  }
}

scenario "an error should be returned", {

  given "the request and response", {
    url = "${Request.NAMESPACE}base/getmetadata"
    request = new Request(url);
    error = new ErrorResponse(request)
  }

  when "the to js function is executed", {
    result = error.toJavascriptFunction()
  }

  then "the result must match the expected string", {
    result.contains('Krysis.Device.Base.Error.getmetadata')
  }

  and "the parameter code with unknown error number should be present", {
    result.contains("{code: ${ErrorResponse.UNKNOWN}}")
  }
}

scenario "create a new success response without parameters", {

  given "the request and response", {
    url = "${Request.NAMESPACE}base/getmetadata"
    request = new Request(url);
    success = new SuccessResponse(request)
  }

  when "the to javascript function is called", {
    result = success.toJavascriptFunction()
  }

  then "result is as expected", {
    ensure(result) {
      isEqualTo "Krypsis.Device.Base.Success.getmetadata()"
    }
  }
}

scenario "create a new success response with parameters", {

  given "the request and response", {
    url = "${Request.NAMESPACE}base/getmetadata"
    request = new Request(url);
    success = new SuccessResponse(request)
  }

  when "add some parameters to the response", {
    success.addParameter("key1", "value1");
    success.addParameter("key2", 2);
    success.addParameter("key3", 5.7);
  }

  and "call the toJavascriptFunction function", {

  }

  then "the result is not null", {
    result.shouldNotBe null
  }

  and "contains the answer namespace, module context, response type and action", {
    result.contains('Krypsis.Device.Base.Success.getmetadata')
  }

  and "contains the params in an undefined order", {
    result.contains("key1: 'value1'")
    result.contains("key2: 2")
    result.contains("key3: 5.7")
  }
}