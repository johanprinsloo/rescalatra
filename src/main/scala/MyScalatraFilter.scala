package com.example

import org.scalatra._
import java.net.URL
import scalate.ScalateSupport
import org.scalatra.fileupload.FileUploadSupport

/**
 * NOTE: Routes are matched from the bottom up
 */
class MyScalatraFilter extends ScalatraFilter with ScalateSupport with FileUploadSupport {

  val headertag =
  <html>
     <body>
     </body>
   </html>

  val footertag =
    <html>
       <body>
       </body>
    </html>

  get("/") {
       <a>{headertag}
        <h1>Scalatra, test101!</h1>
        <ul>
        <li>Say <a href="hello-scalate">hello to Scalate</a>.</li>
        <li>Say <a href="woof">Woof to Scalate</a>.</li>
        <li>Say <a href="hello/Bob">Hello to Bob</a>.</li>
        </ul>{footertag}</a>
  }

  get("/woof") {
    <html>
      <body>
        <h1>Woof!</h1>
        Say <a href="/hello-scalate">Husky alert!</a>.
        <lu>
        <li>Remote Host: {request.getRemoteHost}</li>
        <li>Remote User: {request.getRemoteUser}</li>
        </lu>
      </body>
    </html>
  }

  get("/halt") {
    halt(401, "Go away!")  //stops filter execution here
  }

  get("/guess/*") {
    "You missed!"
  }

  get("/guess/:who") {
    params("who") match {
      case "Frank" => pass()
      case _ => "You got me!"
    }
  }

  get("/hello/:name") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    <p>Hello, {params("name")}</p>
  }

  //files
  get("/file") {
    <form method="post" enctype="multipart/form-data">
        <input type="file" name="foo"/>
        <input type="submit"/>
    </form>
  }

  post("/file") {
    params.getOrElse("file", "")
    //processFile(fileParams("file"))
  }

  post("""/multipart.*""".r) {
    params.get("string") foreach { response.setHeader("string", _) }
    fileParams.get("file") foreach { fi => response.setHeader("file", new String(fi.get)) }
    fileParams.get("file-none") foreach { fi => response.setHeader("file-none", new String(fi.get)) }
    fileParams.get("file-multi") foreach { fi => response.setHeader("file-multi", new String(fi.get)) }
    fileMultiParams.get("file-multi") foreach { fis =>
      response.setHeader("file-multi-all", fis.foldLeft(""){ (acc, fi) => acc + new String(fi.get) })
    }
    params.get("file") foreach { response.setHeader("file-as-param", _) }
  }

  notFound {
    // If no route matches, then try to render a Scaml template
    val templateBase = requestPath match {
      case s if s.endsWith("/") => s + "index"
      case s => s
    }
    val templatePath = "/WEB-INF/scalate/templates/" + templateBase + ".scaml"
    servletContext.getResource(templatePath) match {
      case url: URL => 
        contentType = "text/html"
        templateEngine.layout(templatePath)
      case _ => 
        filterChain.doFilter(request, response)
    } 
  }
}
