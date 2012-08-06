<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="artist">
	<html>
	<head>
	<meta charset="UTF-8"/>
	<title>アーティスト</title>
	</head>
	<body>
	<xsl:apply-templates/>
	</body>
	</html>
</xsl:template>
<xsl:template match="artist/name">
	<div>アーティスト名：<xsl:apply-templates/></div>
</xsl:template>
<xsl:template match="artist/label">
	<div>所属レコード会社：<xsl:apply-templates/></div>
</xsl:template>
</xsl:stylesheet>