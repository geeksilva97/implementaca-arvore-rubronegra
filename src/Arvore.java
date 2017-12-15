
public class Arvore {
	public No raiz;
	public static No nil = new No(0, false);
	public Integer cont;

	public Arvore() {
		this.raiz = Arvore.nil;
	}

	public Arvore(int v) {
		this.raiz = new No(v, false);
	}

	private void rotacao_esq(No x) {
		No y = x.dir;
		x.dir = y.esq;
		if (y.esq != Arvore.nil)
			y.esq.p = x;
		y.p = x.p;
		if (x.p == Arvore.nil)
			this.raiz = y;
		else if (x == x.p.esq)
			x.p.esq = y;
		else
			x.p.dir = y;
		y.esq = x;
		x.p = y;
	}

	private void rotacao_dir(No x) {
		No y = x.esq;
		x.esq = y.dir;
		if (y.dir != Arvore.nil)
			y.dir.p = x;
		y.p = x.p;
		if (x.p == Arvore.nil)
			this.raiz = y;
		else if (x == x.p.esq)
			x.p.esq = y;
		else
			x.p.dir = y;
		y.dir = x;
		x.p = y;
	}

	public void adiciona(int n) {
		if (this.raiz == Arvore.nil) {
			this.raiz = new No(n, false);
		} else {
			No a = this.encontra(n);
			if (n < a.v) {
				a.esq = new No(n, true);
				a.esq.p = a;
				this.fixaadicao(a.esq);
			} else if (n > a.v) {
				a.dir = new No(n, true);
				a.dir.p = a;
				this.fixaadicao(a.dir);
			}
		}
	}

	// Realiza a troca de nós
	public void transplant(No x, No y) {
		if (x.p == Arvore.nil)
			this.raiz = y;
		else if (x == x.p.esq)
			x.p.esq = y;
		else
			x.p.dir = y;
		y.p = x.p;
	}

	private void fixaadicao(No z) {
		No y;
		while (z.p.cor) {
			if (z.p == z.p.p.esq) {
				y = z.p.p.dir;
				if (y.cor) {
					z.p.cor = false;
					y.cor = false;
					z.p.p.cor = true;
					z = z.p.p;
				} else {
					if (z == z.p.dir) {
						z = z.p;
						this.rotacao_esq(z);
					}
					z.p.cor = false;
					z.p.p.cor = true;
					this.rotacao_dir(z.p.p);
				}
			} else {
				y = z.p.p.esq;
				if (y.cor) {
					y.cor = z.p.cor = false;
					z.p.p.cor = true;
					z = z.p.p;
				} else {
					if (z == z.p.esq) {
						z = z.p;
						this.rotacao_dir(z);
					}
					z.p.cor = false;
					z.p.p.cor = true;
					this.rotacao_esq(z.p.p);
				}
			}
		}
		this.raiz.cor = false;
	}

	public void remove(int n) {
		No z = this.encontra(n);
		No x, y = z;
		boolean cordey = y.cor;

		if (z.v == n) {
			if (z.esq == Arvore.nil) {
				x = z.dir;
				this.transplant(z, z.dir);
			} else if (z.dir == Arvore.nil) {
				x = z.esq;
				this.transplant(z, z.esq);
			} else {
				y = z.sucessor();
				cordey = y.cor;
				x = y.dir;

				if (y.p == z)
					x.p = y;
				else {
					this.transplant(y, y.dir);
					y.dir = z.dir;
					y.dir.p = y;
				}
				this.transplant(z, y);
				y.esq = z.esq;
				y.esq.p = y;
				y.cor = z.cor;
			}

			if (!cordey)
				this.fixaremocao(x);
		}
	}

	private void fixaremocao(No n) {
		No x;

		while (n != this.raiz && !n.cor) {
			if (n == n.p.esq) {
				x = n.p.dir;

				if (x.cor) { // caso 1
					x.cor = false;
					n.p.cor = true;
					this.rotacao_esq(n.p);
					x = n.p.dir;
				}
				if (!x.esq.cor && !x.dir.cor) { // caso 2
					x.cor = true;
					n = n.p;
				} else {
					if (!x.dir.cor) { // caso 3
						x.esq.cor = false;
						x.cor = true;
						this.rotacao_dir(x);
						x = n.p.dir;
					}
					// caso 4
					x.cor = n.p.cor;
					n.p.cor = false;
					x.dir.cor = false;
					this.rotacao_esq(n.p);
					n = this.raiz;
				}
			} else {
				x = n.p.esq;

				if (x.cor) { // caso 1
					x.cor = false;
					n.p.cor = true;
					this.rotacao_dir(n.p);
					x = n.p.esq;
				}
				if (!x.esq.cor && !x.dir.cor) { // caso 2
					x.cor = true;
					n = n.p;
				} else {
					if (!x.esq.cor) { // caso 3
						x.dir.cor = false;
						x.cor = true;
						this.rotacao_esq(x);
						x = n.p.esq;
					}
					// caso 4
					x.cor = n.p.cor;
					n.p.cor = false;
					x.esq.cor = false;
					this.rotacao_dir(n.p);
					n = this.raiz;
				}
			}
		}
		n.cor = false;
	}

	public No encontra(int n) {
		return this.raiz.encontra(n);
	}

	void print() {
		print(raiz);
	};

	void print(No n) {
		if (n != null) {
			String v = (n.v == 0) ? "nil" : n.v + "";
			System.out.print(v + "[" + color(n.cor) + "] -->");
			print(n.esq);
			print(n.dir);
		}
	}

	String color(boolean flag) {
		return (flag) ? "RED" : "BLACK";
	}

}
