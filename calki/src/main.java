public class main {
    static double cal_prostokatow=0;
    static double cal_trapzow=0;
    static double cal_simsona=0;
    static double cal_gaussa=0;
    static double[][] wezly_wagi;

    //całka gausa
    abstract static class M_gausa extends Thread
    {
        double ai;
        double bi;
        int i;

        public M_gausa(double ai, double bi, int wagi_iter)
        {
            this.ai=ai;
            this.bi=bi;
            i=wagi_iter;
        }
        abstract double wzor(double x);
        public void run()
        {
            cal_gaussa+=wezly_wagi[1][i]*wzor((((bi-ai)/2)*wezly_wagi[0][i])+((bi+ai)/2));
        }
    }

    //całka prostokatow
    abstract static class M_prostokatow extends Thread
    {
        double ai;
        double bi;
        double h;

        public M_prostokatow(double ai, double bi)
        {
            this.ai=ai;
            this.bi=bi;
            h=(bi-ai)/2;
        }
        abstract double wzor(double x);
        public void run()
        {
            cal_prostokatow+=wzor(ai+(h/2));
        }
    }

    //całka trapezow
    abstract static class M_trapezow extends Thread
    {
        double ai;
        double bi;
        public M_trapezow(double ai, double bi)
        {
            this.ai=ai;
            this.bi=bi;
        }
        abstract double wzor(double x);
        public void run()
        {
            cal_trapzow+=(wzor(ai)+(wzor(bi)));
        }
    }

    //całka simsona
    abstract static class M_simsona extends Thread
    {
        double ai;
        double bi;
        double t;
        public M_simsona(double ai, double bi)
        {
            this.ai=ai;
            this.bi=bi;
            t=(ai+bi)/2;
        }
        abstract double wzor(double x);
        public void run()
        {
            cal_simsona+=(wzor(ai)+(4*wzor(t))+wzor(bi));
        }
    }

    //wzór funkcji którą całkujemy
    static double wzor_ogolny(double x)
    {
        return (x - 1) / (Math.pow(x, 2) + x);
    }


    public static void main(String[] args) throws InterruptedException {
        double a=2;
        double b=3;
        double n=3;
        double h=((b-a)/n);


        //kwadratury gaussa
        Wagi test= new Wagi((int)n);
        wezly_wagi=test.tablica();
        for (int i=0; i<n; i++) {
            Thread calka_gaussa = new M_gausa(2, 3, i) {
                @Override
                double wzor(double x) {
                    return wzor_ogolny(x);
                }
            };
            calka_gaussa.start();
            calka_gaussa.join();
        }
        //Thread.sleep(300);
        cal_gaussa=cal_gaussa*((b-a)/2);
        System.out.println("calka gaussa: "+cal_gaussa);


        //metoda prostokatow
        for (int i=0; i<n; i++)
        {
            Thread calka_prostokaty= new M_prostokatow(a + ((i / n) * (b - a)),a + (((i + 1) / n) * (b - a))) {
                @Override
                double wzor(double x) {
                    return wzor_ogolny(x);
                }
            };
            calka_prostokaty.start();
            calka_prostokaty.join();
        }
        //Thread.sleep(300);
        cal_prostokatow=cal_prostokatow*h;
        System.out.println("Calka prostokatow: "+cal_prostokatow);


        //metoda trapezow
        for (int i=0; i<n; i++)
        {
            Thread calka_trapezy= new M_trapezow(a + ((i / n) * (b - a)),a + (((i + 1) / n) * (b - a))) {
                @Override
                double wzor(double x) {
                    return wzor_ogolny(x);
                }
            };
            calka_trapezy.start();
            calka_trapezy.join();
        }
        //Thread.sleep(300);
        cal_trapzow=(cal_trapzow*h)/2;
        System.out.println("Calka trapezow: "+cal_trapzow);


        //metoda simsona
        h=h/2;
        for (int i=0; i<n; i++)
        {
            Thread calka_simson= new M_simsona(a + ((i / n) * (b - a)),a + (((i + 1) / n) * (b - a))) {
                @Override
                double wzor(double x) {
                    return wzor_ogolny(x);
                }
            };
            calka_simson.start();
            calka_simson.join();
        }
        //Thread.sleep(300);
        cal_simsona=(cal_simsona*h)/3;
        System.out.println("Calka simsona: "+cal_simsona);

    }
}
