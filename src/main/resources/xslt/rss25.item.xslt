<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:p="http://univ.fr/rss25"
                exclude-result-prefixes="p">

  <!-- Configuration de la sortie HTML -->
  <xsl:output method="html" encoding="UTF-8" indent="yes" doctype-public=""/>

  <!-- Template racine qui produit la page HTML complète -->
  <xsl:template match="/">
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
        <header>
          <h1>Projet RSS25SB</h1>
        </header>
        <main>
          <!-- Choix du contenu à afficher : soit l’article, soit une erreur -->
          <xsl:choose>
            <!-- Cas où un article est présent -->
            <xsl:when test="p:item">
              <xsl:apply-templates select="p:item"/>
            </xsl:when>
            <!-- Cas où une erreur est présente -->
            <xsl:when test="p:error">
              <xsl:apply-templates select="p:error"/>
            </xsl:when>
          </xsl:choose>
        </main>
      </body>
    </html>
  </xsl:template>

  <!-- Template d'affichage d’un article valide -->
  <xsl:template match="p:item">
    <section>
      <h2>Détail de l’article</h2>
      <div>
        <!-- Titre de l’article -->
        <p>
            <strong>Titre :</strong>
            <xsl:value-of select="normalize-space(p:title)"/>
        </p>

        <!-- GUID sous forme de lien -->
        <p><strong>GUID :</strong>
          <a href="{p:guid}" target="_blank">
            <xsl:value-of select="normalize-space(p:guid)"/>
          </a>
        </p>

        <!-- Date de publication (si présente) -->
        <xsl:if test="p:published">
          <p><strong>Publié :</strong> <xsl:value-of select="normalize-space(p:published)"/></p>
        </xsl:if>

        <!-- Date de mise à jour (si présente) -->
        <xsl:if test="p:updated">
          <p><strong>Mise à jour :</strong> <xsl:value-of select="normalize-space(p:updated)"/></p>
        </xsl:if>

        <!-- Liste des catégories (si présentes) -->
        <xsl:if test="p:category">
          <p><strong>Catégories :</strong></p>
          <ul>
            <xsl:for-each select="p:category">
              <li><xsl:value-of select="@term"/></li>
            </xsl:for-each>
          </ul>
        </xsl:if>

        <!-- Contenu -->
        <xsl:if test="p:content">
          <p><strong>Contenu :</strong> <xsl:value-of select="normalize-space(p:content/@src)"/></p>
        </xsl:if>

        <!-- Auteurs et contributeurs -->
        <xsl:if test="p:author or p:contributor">
          <p><strong>Auteur(s) / Contributeur(s) :</strong></p>
          <ul>
            <xsl:for-each select="p:author | p:contributor">
              <li>
                <xsl:value-of select="@name"/>
                <xsl:if test="@email"> (<xsl:value-of select="@email"/>)</xsl:if>
              </li>
            </xsl:for-each>
          </ul>
        </xsl:if>

        <!-- Image (si présente) -->
        <xsl:if test="p:image">
          <p><strong>Image :</strong></p>
          <img src="{p:image/@href}" alt="{p:image/@alt}"/>
        </xsl:if>
      </div>
    </section>
  </xsl:template>

  <!-- Template d'affichage d’une erreur -->
  <xsl:template match="p:error">
    <section>
      <h2>Détail de l’article</h2>
      <div>
        <!-- Affichage de l'erreur avec mise en forme rouge -->
        <p style="color: red;">
          <strong>Erreur :</strong> Article <xsl:value-of select="@id"/> introuvable. Status = ERROR
        </p>
      </div>
    </section>
  </xsl:template>

</xsl:stylesheet>