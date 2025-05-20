<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!-- Configuration de la sortie HTML -->
  <xsl:output method="html" encoding="UTF-8" indent="yes"/>

  <!-- Template principal appliqué à la racine <inserted> du document XML -->
  <xsl:template match="/inserted">
    <html>
      <head>
        <title>Résultat de l'insertion</title>
        <meta charset="UTF-8"/>
        <!-- Feuille de style externe -->
        <link rel="stylesheet" href="/css/style.css"/>
      </head>
      <body>
        <h2>Résultat de l'insertion</h2>

        <!-- Choix selon le statut retourné : INSERTED ou ERROR -->
        <xsl:choose>
          <!-- Si le statut est "INSERTED", afficher le succès -->
          <xsl:when test="status = 'INSERTED'">
            <p style="color:green;">Insertion réussie.</p>

            <!-- Afficher la liste des ID insérés s’ils existent -->
            <xsl:if test="ids/id">
              <p>Articles insérés :</p>
              <ul>
                <!-- Boucle sur chaque ID inséré -->
                <xsl:for-each select="ids/id">
                  <li>Article ID : <xsl:value-of select="."/></li>
                </xsl:for-each>
              </ul>
            </xsl:if>
          </xsl:when>

          <!-- Sinon, c’est un échec : afficher le message d'erreur -->
          <xsl:otherwise>
            <p style="color:red;">Insertion échouée.</p>
            <!-- Si une description d'erreur est disponible, l'afficher -->
            <xsl:if test="description">
              <pre><xsl:value-of select="description"/></pre>
            </xsl:if>
          </xsl:otherwise>
        </xsl:choose>

        <hr/>
        <!-- Lien retour vers le formulaire de téléversement -->
        <a href="/rss25SB/insert">Retour au formulaire</a>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>