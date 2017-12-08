package com.aemtools.inspection.html

import com.aemtools.inspection.service.IInspectionService
import com.aemtools.test.util.mock
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.project.Project
import com.intellij.psi.xml.XmlAttribute
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.lifecycle.CachingMode
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.picocontainer.PicoContainer

/**
 * Specification for [MessedDataSlyAttributeInspection].
 *
 * @author Dmytro Troynikov
 */
object MessedDataSlyAttributeInspectionSpec : Spek({
  val tested = MessedDataSlyAttributeInspection()

  given("the inspection") {

    on("style check") {
      it("should have correct group display name") {
        assertThat(tested.groupDisplayName)
            .isEqualTo("HTL")
      }

      it("should have correct display name") {
        assertThat(tested.displayName)
            .isEqualTo("data-sly-attribute with prohibited attributes")
      }

      it("should have correct static description") {
        assertThat(tested.staticDescription)
            .isEqualTo("""
<html>
<body>
This inspection verifies that <i>data-sly-attribute</i> is
<b>not</b> used with prohibited attributes, such as <b>style</b> or event attributes i.e.
attributes that take JavaScript as input (e.g. onclick, onmousemove, etc).
</body>
</html>""".trimIndent())
      }
    }

  }

  given("check attribute") {

    val project by memoized(CachingMode.TEST) { mock<Project>() }
    val attribute: XmlAttribute = mock()
    val holder: ProblemsHolder = mock()
    val picoContainer: PicoContainer = mock()
    val inspectionService: IInspectionService by memoized(CachingMode.TEST) { mock<IInspectionService>() }
    beforeGroup {
      whenever(project.picoContainer)
          .thenReturn(picoContainer)
      whenever(picoContainer.getComponentInstance(IInspectionService::class.java.name))
          .thenReturn(inspectionService)
      whenever(attribute.project)
          .thenReturn(project)
    }

    on("wrong attribute") {
      val wrongAttributes = listOf("style",
          "onafterprint",
          "onbeforeprint",
          "onbeforeunload",
          "onerror",
          "onhashchange",
          "onload",
          "onmessage",
          "onoffline",
          "ononline",
          "onpagehide",
          "onpageshow",
          "onpopstate",
          "onresize",
          "onstorage",
          "onunload",
          "onblur",
          "onchange",
          "oncontextmenu",
          "onfocus",
          "oninput",
          "oninvalid",
          "onreset",
          "onsearch",
          "onselect",
          "onsubmit",
          "onkeydown",
          "onkeypress",
          "onkeyup",
          "onclick",
          "ondblclick",
          "onmousedown",
          "onmousemove",
          "onmouseout",
          "onmouseover",
          "onmouseup",
          "onmousewheel",
          "onwheel",
          "ondrag",
          "ondragend",
          "ondragenter",
          "ondragleave",
          "ondragover",
          "ondragstart",
          "ondrop",
          "onscroll",
          "oncopy",
          "oncut",
          "onpaste",
          "onabort",
          "oncanplay",
          "oncanplaythrough",
          "oncuechange",
          "ondurationchange",
          "onemptied",
          "onended",
          "onerror",
          "onloadeddata",
          "onloadedmetadata",
          "onloadstart",
          "onpause",
          "onplay",
          "onplaying",
          "onprogress",
          "onratechange",
          "onseeked",
          "onseeking",
          "onstalled",
          "onsuspend",
          "ontimeupdate",
          "onvolumechange",
          "onwaiting",
          "onshow",
          "ontoggle"
      )

      wrongAttributes.forEach { wrongAttribute ->
        it("should report $wrongAttribute") {
          `when`(attribute.name)
              .thenReturn("data-sly-attribute.$wrongAttribute")
          `when`(inspectionService.validTarget(any()))
              .thenReturn(true)

          tested.checkAttribute(attribute, holder, false)

          verify(inspectionService)
              .messedDataSlyAttribute(
                  holder,
                  attribute,
                  wrongAttribute
              )
        }
      }

    }

    on("correct attribute") {
      whenever(inspectionService.validTarget(any()))
          .thenReturn(true)
      whenever(attribute.name)
          .thenReturn("data-sly-attribute.correct")
      it("should do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
            .messedDataSlyAttribute(
                any(), any(), any()
            )
      }
    }

    on("not data-sly-attribute") {
      whenever(inspectionService.validTarget(any()))
          .thenReturn(true)
      whenever(attribute.name)
          .thenReturn("style")
      it("should do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
            .messedDataSlyAttribute(
                any(), any(), any()
            )
      }
    }

    on("inspection disabled") {
      whenever(inspectionService.validTarget(any()))
          .thenReturn(false)
      it("should do nothing") {
        tested.checkAttribute(attribute, holder, false)

        verify(inspectionService, never())
            .messedDataSlyAttribute(
                any(), any(), any()
            )
      }
    }

  }

})
