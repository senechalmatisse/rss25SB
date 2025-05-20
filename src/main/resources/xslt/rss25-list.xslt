<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:p="http://univ.fr/rss25"
  exclude-result-prefixes="p">

    <!-- Définition du format de sortie -->
    <xsl:output method="html" encoding="utf-8" indent="yes" />

    <!-- Template principal -->
    <xsl:template match="/items">
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;</xsl:text>
        <html lang="fr">
            <head>
                <title>Liste des articles RSS</title>
                <link rel="stylesheet" type="text/css" href="/css/style.css"/>
            </head>
            <body>
                <header>
                    <h1>Projet RSS25SB</h1>
                </header>

                <main>
                    <section>
                        <h2>Résumé des articles </h2>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Titre</th>
                                    <th>GUID</th>
                                    <th>Date</th>
                                </tr>
                            </thead>
                            <tbody>
                                <xsl:apply-templates select="item"/>
                            </tbody>
                        </table>
                    </section>
                </main>
            </body>
        </html>
    </xsl:template>

    <!-- Template pour chaque article -->
    <xsl:template match="item">
        <tr>
            <td><xsl:value-of select="id"/></td>
            <td><xsl:value-of select="title"/></td>
            <td>
                <a href="{guid}" target="_blank">
                    <xsl:value-of select="guid"/>
                </a>
            </td>
            <td><xsl:value-of select="date"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>