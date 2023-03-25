package dataSource;

import java.sql.*;

public class SourceDeDonnees {
	Connection connexion;

	public void connexionBD() {
		try {
			// Charger le driver JDBC
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Établir la connexion à la base de données
			String url = "jdbc:mysql://localhost:3306/bd_escapegame";
			String utilisateur = "root";
			String motDePasse = "";
			connexion = DriverManager.getConnection(url, utilisateur, motDePasse);

			// Tester la connexion
			if (connexion != null) {
				System.out.println("Connexion à la base de données réussie !");
			} else {
				System.out.println("Erreur lors de la connexion à la base de données.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void ajouter_joueur(String prenom, String nom, String pseudo, String mdp) throws SQLException {

		this.connexionBD();

		String req = "INSERT INTO joueur(prenom, nom, pseudo, password) values(?,?,?,?)";
		PreparedStatement ps = connexion.prepareStatement(req);
		ps.setString(1, prenom);
		ps.setString(2, nom);
		ps.setString(3, pseudo);
		ps.setString(4, mdp);

		ps.executeUpdate();
	}

	public boolean verifier_joueur(String pseudo, String mdp) {

		this.connexionBD();

		boolean res = false;
		try {
			Statement stmt = connexion.createStatement();
			String req = "SELECT * FROM joueur WHERE Pseudo='" + pseudo + "' and MotDePasse='" + mdp + "'";
			ResultSet rs = stmt.executeQuery(req);
			res = rs.next();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
