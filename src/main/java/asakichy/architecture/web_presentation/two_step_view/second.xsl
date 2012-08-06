<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="album">
	<html>
	<head>
	<meta charset="UTF-8"/>
	<title>アルバム</title>
	</head>
	<body>
	<xsl:apply-templates/>
	</body>
	</html>
</xsl:template>
<xsl:template match="title">
	<h1><xsl:apply-templates/></h1>
</xsl:template>
<xsl:template match="field">
	<div><xsl:value-of select="@label"/>：<xsl:apply-templates/></div>
</xsl:template>
<xsl:template match="table">
	<table><xsl:apply-templates/></table>
</xsl:template>
<xsl:template match="table/row">
	<tr><xsl:apply-templates/></tr>
</xsl:template>
<xsl:template match="table/row/cell">
	<td><xsl:apply-templates/></td>
</xsl:template>
</xsl:stylesheet>