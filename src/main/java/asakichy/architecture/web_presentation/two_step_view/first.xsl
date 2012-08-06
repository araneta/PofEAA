<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:template match="album">
	<album><xsl:apply-templates/></album>
</xsl:template>
<xsl:template match="album/title">
	<title><xsl:apply-templates/></title>
</xsl:template>
<xsl:template match="artist">
	<field label="アーティスト"><xsl:apply-templates/></field>
</xsl:template>
<xsl:template match="trackList">
	<table><xsl:apply-templates/></table>
</xsl:template>
<xsl:template match="track">
	<row><xsl:apply-templates/></row>
</xsl:template>
<xsl:template match="track/title">
	<cell><xsl:apply-templates/></cell>
</xsl:template>
<xsl:template match="track/time">
	<cell><xsl:apply-templates/></cell>
</xsl:template>
</xsl:stylesheet>
