package com.example

import org.scalatra._
import java.net.URL
import scalate.ScalateSupport

class MyScalatraFilter extends ScalatraFilter with ScalateSupport {

  val headertag =
  <html>
    <body>

  val footertag =
      </body>
    </html>

  get("/") {
    <html>
      <body>
        <h1>Scalatra, test101!</h1>
        <ul>
        <li>Say <a href="hello-scalate">hello to Scalate</a>.</li>
        <li>Say <a href="woof">Woof to Scalate</a>.</li>
        <li>Say <a href="hello/Bob">Hello to Bob</a>.</li>
        </ul>
      </body>
    </html>
  }

  get("/woof") {
    <html>
      <body>
        <h1>Woof!</h1>
        Say <a href="/hello-scalate">Husky alert!</a>.
      </body>
    </html>
  }

  get("/hello/:name") {
    // Matches "GET /hello/foo" and "GET /hello/bar"
    // params("name") is "foo" or "bar"
    <p>Hello, {params("name")}</p>
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
