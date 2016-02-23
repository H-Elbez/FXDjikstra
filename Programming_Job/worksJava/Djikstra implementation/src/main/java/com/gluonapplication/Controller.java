package com.gluonapplication;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Controller implements Initializable {
	boolean addn, adda, cal, exi, first_arc, exist;
	Noeud[] noeud;
	Noeud h;
	String victim, elt, source;
	int Name, ii, poids, x;
	Line l, path_final;
	List<Noeud> path;
	Thread th ;
	List<Timeline> anims;
	int i;
	double x1, y1, x2, y2, xdiv, ydiv;
	Circle c;
	Label text;
	Timeline t;
	Timeline[] tlist;
	SequentialTransition st ;
	@FXML
	TextField poid;
	@FXML
	Label tv_poid, affichage;
	@FXML
	private Button ajoutera, ajoutern, supprimern, calculer, valider ,Exit;
	@FXML
	private AnchorPane calque;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		addn = adda = false;
		i = 0;
		Name = 1;
		tv_poid.setOpacity(0);
		poid.setOpacity(0);
		affichage.setOpacity(0);
		valider.setOpacity(0);
		poid.setEditable(false);
		noeud = new Noeud[100];
		ajoutern.setStyle("-fx-background-color : #3754FE");
		ajoutera.setStyle("-fx-background-color : #3754FE");
		Exit.setStyle("-fx-background-color : #3754FE");
		supprimern.setStyle("-fx-background-color : #3754FE");
		calculer.setStyle("-fx-background-color : #3754FE");
		calque.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				try {
					if (addn) {
						if (!exist(event.getX(), event.getY(), i)) {
							i++;
							t = new Timeline();
							c = new Circle(event.getX(), event.getY(), 20);
							c.setOpacity(0);
							t.getKeyFrames()
									.add(new KeyFrame(Duration.seconds(1), new KeyValue(c.opacityProperty(), 1)));
							c.setFill(Paint.valueOf("#2E3097"));
							text = new Label(String.valueOf(i), c);
							text.setLayoutX(event.getX() - 20);
							text.setStyle("-fx-font-size : 17px;");
							text.setLayoutY(event.getY() - 20);
							calque.getChildren().add(text);
							t.play();
							noeud[i] = new Noeud(String.valueOf(Name));
							noeud[i].x = event.getX();
							noeud[i].y = event.getY();
							Name++;

						}
					}
					if (adda) {
						try {
							if (i > 1) {
								victim = find_elt(event.getX(), event.getY());
								if (victim != "") {
									if (first_arc) {
										elt = victim;
										x1 = noeud[Integer.valueOf(victim)].x;
										y1 = noeud[Integer.valueOf(victim)].y;
										first_arc = !first_arc;
										c = new Circle(x1, y1, 20);
										c.setOpacity(1);
										c.setFill(Paint.valueOf("#000000"));
										calque.getChildren().add(c);
									} else {
										x2 = noeud[Integer.valueOf(victim)].x;
										y2 = noeud[Integer.valueOf(victim)].y;
										tv_poid.setOpacity(1);
										poid.setOpacity(1);
										valider.setOpacity(1);
										poid.setEditable(true);
									}
								}
							}
						} catch (Exception e) {
							first_arc = true;
							tv_poid.setOpacity(0);
							poid.setOpacity(0);
							valider.setOpacity(0);
						}
					}
					if (cal) {
						victim = find_elt(event.getX(), event.getY());
						if (victim != "") {
							if (first_arc) {
								affichage.setOpacity(0);
								calque.getChildren().clear();
								init();
								c = new Circle(noeud[Integer.valueOf(victim)].x, noeud[Integer.valueOf(victim)].y, 20);
								c.setOpacity(1);
								c.setFill(Paint.valueOf("#000000"));
								calque.getChildren().add(c);
								Dijikstra.computePaths(noeud[Integer.valueOf(victim)]);
								first_arc = !first_arc;
								source = victim;
							} else {
								anims = new ArrayList<Timeline>();
								path = Dijikstra.getShortestPathTo(noeud[Integer.valueOf(victim)]);
								c = new Circle(noeud[Integer.valueOf(victim)].x, noeud[Integer.valueOf(victim)].y, 20);
								c.setOpacity(1);
								c.setFill(Paint.valueOf("#000000"));
								calque.getChildren().add(c);
								Collections.reverse(path);
								
								drawing(noeud[Integer.valueOf(victim)]);
								st = new SequentialTransition(tlist);
								st.play();
								
								
								first_arc = !first_arc;
								if (noeud[Integer.valueOf(victim)].minDistance != Double.POSITIVE_INFINITY) {
									affichage.setText("Distance : " + noeud[Integer.valueOf(victim)].minDistance);
								} else {
									affichage.setText("Distance : 0");
								}
								affichage.setOpacity(1);
								
							}
						}
					}
				} catch (Exception e) {
					first_arc = !first_arc;
				}
			}
		});
	}

	@FXML
	private void ajouterNoeud() {
		addn = true;
		adda = cal = false;
		tv_poid.setOpacity(0);
		poid.setOpacity(0);
		valider.setOpacity(0);
		ajoutern.setStyle("-fx-background-color : #FA0B4D");
		ajoutera.setStyle("-fx-background-color : #3754FE");
		supprimern.setStyle("-fx-background-color : #3754FE");
		calculer.setStyle("-fx-background-color : #3754FE");
	}
	@FXML
	private void Exitapp(){
		try{
		Platform.exit();}catch(Exception e){
			
		}
	}
	@FXML
	private void ajouterArc() {
		try {
			adda = true;
			addn = cal = false;
			first_arc = true;
			tv_poid.setOpacity(0);
			poid.setOpacity(0);
			valider.setOpacity(0);
			calque.getChildren().clear();
			init();
			ajoutera.setStyle("-fx-background-color : #FA0B4D");
			supprimern.setStyle("-fx-background-color : #3754FE");
			ajoutern.setStyle("-fx-background-color : #3754FE");
			calculer.setStyle("-fx-background-color : #3754FE");
		} catch (Exception e) {
		}

	}

	@FXML
	private void SupprimerNoeud() {
		calculer.setStyle("-fx-background-color : #3754FE");
		supprimern.setStyle("-fx-background-color : #3754FE");
		ajoutera.setStyle("-fx-background-color : #3754FE");
		ajoutern.setStyle("-fx-background-color : #3754FE");
		calculer.setStyle("-fx-background-color : #3754FE");
		tv_poid.setOpacity(0);
		poid.setOpacity(0);
		valider.setOpacity(0);
		calque.getChildren().clear();
		affichage.setOpacity(0);
		i = 0;
		Name = 1;
		noeud = null;
		noeud = new Noeud[100];
	}

	@FXML
	private void set_weight() {
		try {
			exist = false;
			x = 1;
			while (x <= noeud[Integer.valueOf(victim)].i && !exist) {
				if (noeud[Integer.valueOf(victim)].adjacents[x].target.name == noeud[Integer.valueOf(elt)].name) {
					exist = true;
				}
				x++;
			}
			if (!exist) {
				poids = Integer.valueOf(poid.getText());
				if(poids >= 0){
				noeud[Integer.valueOf(victim)].add_arc(noeud[Integer.valueOf(elt)], poids);
				noeud[Integer.valueOf(elt)].add_arc(noeud[Integer.valueOf(victim)], poids);
				l = new Line(x1, y1, x2, y2);
				l.setStrokeWidth(3);
				if ((poid.getText() != "")) {
					t = new Timeline();
					l.setOpacity(0);
					t.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), new KeyValue(l.opacityProperty(), 1)));
					l.setStroke(Paint.valueOf("#2E3097"));
					text = new Label(poid.getText());
					xdiv = noeud[Integer.valueOf(victim)].x - noeud[Integer.valueOf(elt)].x;
					xdiv = xdiv / 2;
					if (xdiv >= 0) {
						text.setLayoutX(xdiv + noeud[Integer.valueOf(elt)].x);
					} else {
						text.setLayoutX(-xdiv + noeud[Integer.valueOf(victim)].x);
					}
					ydiv = noeud[Integer.valueOf(victim)].y - noeud[Integer.valueOf(elt)].y;
					ydiv = ydiv / 2;
					if (ydiv >= 0) {
						text.setLayoutY(ydiv + noeud[Integer.valueOf(elt)].y);
					} else {
						text.setLayoutY(-ydiv + noeud[Integer.valueOf(victim)].y);
					}
					text.setStyle("-fx-font-size : 19px;");
					text.setTextFill(Paint.valueOf("FA0B4D"));
					calque.getChildren().add(l);
					calque.getChildren().add(text);
					t.play();
					first_arc = !first_arc;
				} else {
					first_arc = !first_arc;
				}
				}else{
					tv_poid.setOpacity(0);
					poid.setOpacity(0);
					valider.setOpacity(0);
					first_arc = !first_arc;
					poid.setText("");
					calque.getChildren().clear();
					init();
				}
			}
			tv_poid.setOpacity(0);
			poid.setOpacity(0);
			valider.setOpacity(0);
			poid.setText("");
			calque.getChildren().clear();
			init();
		} catch (Exception e) {
			tv_poid.setOpacity(0);
			poid.setOpacity(0);
			valider.setOpacity(0);
			first_arc = !first_arc;
			poid.setText("");
			calque.getChildren().clear();
			init();
		}
	}

	@FXML
	private void CalculerChemin() {
		tv_poid.setOpacity(0);
		poid.setOpacity(0);
		valider.setOpacity(0);
		supprimern.setStyle("-fx-background-color : #3754FE");
		ajoutera.setStyle("-fx-background-color : #3754FE");
		ajoutern.setStyle("-fx-background-color : #3754FE");
		calculer.setStyle("-fx-background-color : #FA0B4D");
		cal = !cal;
		addn = adda = false;
		first_arc = true;
	}

	boolean exist(double x, double y, int i) {
		exi = false;
		ii = 1;
		if (i != 0) {
			while (ii <= i && !exi) {
				if (noeud[ii].x - 40.0 < x && noeud[ii].x + 40.0 > x && noeud[ii].y - 40.0 < y
						&& noeud[ii].y + 40.0 > y) {
					exi = true;
				}
				ii++;
			}
		}
		return exi;
	}

	String find_elt(double x, double y) {
		try {
			exi = false;
			ii = 1;
			if (i != 0) {
				while (ii <= i && !exi) {
					if (noeud[ii].x - 40.0 < x && noeud[ii].x + 40.0 > x && noeud[ii].y - 40.0 < y
							&& noeud[ii].y + 40.0 > y) {
						exi = true;
					} else {
						ii++;
					}
				}
				return noeud[ii].name;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}

	void supp_noeud(String name) {

		try {
			ii = 1;
			while (noeud[ii].name != name) {
				ii++;
			}
			for (int k = ii; k < i; k++) {
				noeud[k] = noeud[k + 1];
			}
			noeud[i] = null;
			i--;
			for (int o = 1; o <= i; o++) {
				for (int kk = 1; kk < noeud[o].i; kk++) {
					if (noeud[o].adjacents[kk].target.name == name) {
						for (int adj = kk; adj < noeud[o].i; adj++) {
							noeud[o].adjacents[kk] = noeud[o].adjacents[kk + 1];
						}
						noeud[o].adjacents[noeud[o].i] = null;
						noeud[o].i--;
					}
				}
			}
		} catch (Exception e) {

		}
	}

	void revalidation() {
		for (int k = 1; k <= i; k++) {
			c = new Circle(noeud[k].x, noeud[k].y, 20);
			c.setFill(Paint.valueOf("#2E3097"));
			text = new Label(String.valueOf(k), c);
			text.setLayoutX(noeud[k].x - 20);
			text.setLayoutY(noeud[k].y - 20);
			text.setStyle("-fx-font-size : 17px;");
			calque.getChildren().add(text);

		}
	}

	void init() {
		for (int k = 1; k <= i; k++) {
			for (int bb = 1; bb <= noeud[k].i; bb++) {
				l = new Line(noeud[k].x, noeud[k].y, noeud[k].adjacents[bb].target.x, noeud[k].adjacents[bb].target.y);
				l.setStrokeWidth(3);
				l.setStroke(Paint.valueOf("#2E3097"));
				text = new Label(String.valueOf((int) noeud[k].adjacents[bb].poids));
				xdiv = noeud[k].x - noeud[k].adjacents[bb].target.x;
				xdiv = xdiv / 2;
				if (xdiv >= 0) {
					text.setLayoutX(xdiv + noeud[k].adjacents[bb].target.x);
				} else {
					text.setLayoutX(-xdiv + noeud[k].x);
				}
				ydiv = noeud[k].y - noeud[k].adjacents[bb].target.y;
				ydiv = ydiv / 2;
				if (ydiv >= 0) {
					text.setLayoutY(ydiv + noeud[k].adjacents[bb].target.y);
				} else {
					text.setLayoutY(-ydiv + noeud[k].y);
				}
				text.setStyle("-fx-font-size : 19px;");
				text.setTextFill(Paint.valueOf("FA0B4D"));
				calque.getChildren().add(l);
				calque.getChildren().add(text);
			}
		}
		for (int b = 1; b <= i; b++) {
			noeud[b].minDistance = Double.POSITIVE_INFINITY;
			noeud[b].prevN = 0;
			noeud[b].previous = null;
		}
		revalidation();
	}
	void drawing(Noeud n){
		h = n ;
		x = 0 ;
		while(h != null){
			x++;
			h = h.previous;
		}
		x = (x*2)-2;
		tlist = new Timeline[x+1];
		    while(n != null) {
			c = new Circle(n.x, n.y, 10);
			c.setOpacity(0);
			c.setStrokeWidth(0);
			c.setFill(Paint.valueOf("#FA0B4D"));
		
			t = new Timeline();
					t.getKeyFrames()
							.add(new KeyFrame(Duration.seconds(3).divide(4),
									new KeyValue(c.strokeWidthProperty(), 3),
									new KeyValue(c.opacityProperty(), 1)));
							calque.getChildren().add(c);
			tlist[x] = t ;
			x--;
			if (n.previous != null) {
				path_final = new Line(n.x, n.y, n.previous.x, n.previous.y);
				t = new Timeline();
				l.setOpacity(0);
				path_final.setStrokeWidth(0);
				t.getKeyFrames()
						.add(new KeyFrame(Duration.seconds(3).divide(2),
								new KeyValue(path_final.strokeWidthProperty(), 3),
								new KeyValue(path_final.opacityProperty(), 1)));
				path_final.setStroke(Paint.valueOf("#FA0B4D"));
				calque.getChildren().add(path_final);
				tlist[x] = t ;
				x--;
			}
			n = n.previous;
		    }
	}
}