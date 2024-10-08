package com.aemtools.codeinsight.xml.schema

import com.aemtools.inspection.xml.AemMissedCqNamespaceInspection
import com.aemtools.test.base.BaseLightTest

class AemVaultNamespaceAnnotatorTest : BaseLightTest() {
  fun `test namespace highlighting`() {
    myFixture.enableInspections(AemMissedCqNamespaceInspection())

    myFixture.configureByText("_cq_dialog.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <<info descr="null">jcr</info>:root xmlns:<info descr="null">jcr</info>="http://www.jcp.org/jcr/1.0"
          xmlns:<info descr="null">cq</info>="http://www.day.com/jcr/cq/1.0" <info descr="null">jcr</info>:primaryType="nt:unstructured"></<info descr="null">jcr</info>:root>
    """)
    myFixture.checkHighlighting(true, true, true)
  }

  fun `test namespace highlighting without jcr_primaryType`() {
    myFixture.enableInspections(AemMissedCqNamespaceInspection())

    myFixture.configureByText("_cq_dialog.xml", """<?xml version="1.0" encoding="UTF-8"?>
      <weak_warning descr="Node must have 'jcr:primaryType' attribute."><<info descr="null">jcr</info>:root xmlns:<info descr="null">jcr</info>="http://www.jcp.org/jcr/1.0"
          xmlns:<info descr="null">cq</info>="http://www.day.com/jcr/cq/1.0"></<info descr="null">jcr</info>:root></weak_warning>
    """)
    myFixture.checkHighlighting(true, true, true)
  }
}
