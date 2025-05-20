<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Configuration de la sortie HTML -->
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <!-- Template appliqué à la racine du document XML (<error>) -->
  <xsl:template match="/error">
    <!-- Insertion du doctype HTML -->
    <xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
    <html lang="fr">
      <head>
        <meta charset="UTF-8"/>
        <title>Détail de l'article</title>
        <!-- Feuille de style externe -->
        <link rel="stylesheet" href="/css/style.css"/>
      </head>
      <body>
        <!-- En-tête du site ou de l'application -->
        <header>
            <h1>Projet RSS25SB</h1>
        </header>

        <main>
          <section>
            <h2>Détail de l’article</h2>

            <!-- Bloc contenant le message d’erreur formaté -->
            <div>
              <p style="color: red;">
                <strong>Erreur :</strong> Article
                <xsl:value-of select="id"/> introuvable.
                <br/>

                <strong>Status = </strong>
                <xsl:value-of select="status"/>
                <br/>

                <strong>Description : </strong>
                <xsl:value-of select="description"/>
              </p>
            </div>
          </section>
        </main>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>