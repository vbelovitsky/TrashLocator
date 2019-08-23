package com.java.vbel.trashlocator.temporary;

import androidx.core.util.Pair;
import java.util.Arrays;

public class Kmeans {

    private int[] resultArray;
    private double[][] centersArray;

    public Kmeans(double[][]points, int numClusters){
        Pair<int[], double[][]> resultAndCenters = kMeans(points, numClusters);
        this.resultArray = resultAndCenters.first;
        this.centersArray = resultAndCenters.second;
    }

    //Алгоритм разбиения набора точек на кластеры
    private Pair<int[], double[][]> kMeans(double[][] points, int numClusters){
        int numPoints = points.length;

        //Первоначальные центры кластеров (просто первые numClusters элементов points)
        double[][] centers = new double[numClusters][2];
        System.arraycopy(points, 0, centers, 0, numClusters);

        //Результат - это массив с номерами кластеров для каждой точки
        int[] result = new int[numPoints];
        for (int i = 0; i<numPoints; i++) result[i] = -1;

        //Предыдущая копия массива result, чтобы сравнивать, изменилось что-то или нет
        int[] prevResult = null;

        //Работет, пока кластеры не перестанут меняться
        while(!Arrays.equals(result, prevResult)){
            prevResult = Arrays.copyOf(result, numPoints);

            for (int i = 0; i < numPoints; i++){
                //Минимальная дистанция от точки к центру и индекс этого центра
                double minDist = Double.MAX_VALUE;
                int clusterIndex = Integer.MAX_VALUE;

                for (int j = 0; j < numClusters; j++){
                    //Вычисляем расстояние от точки до центра
                    double distance = Math.pow(points[i][0]-centers[j][0],2) + Math.pow(points[i][1]-centers[j][1],2);
                    if (distance < minDist){
                        minDist = distance;
                        clusterIndex = j;
                    }
                }
                result[i] = clusterIndex;
            }

            //Меняем центры кластеров
            for (int j = 0; j < numClusters; j++){
                double sumX = 0;
                double sumY = 0;
                for (int i = 0; i < numPoints; i++)
                    //Если точка принадлежит кластеру...
                    if (result[i] == j){
                        sumX += points[i][0];
                        sumY += points[i][1];
                    }

                //Считаем, сколько точек принадлежит данному кластеру
                int frequency = 0;
                for (int f = 0; f < result.length; f++) if(result[f] == j) frequency++;

                //Вычисляем x,y для центра
                centers[j][0] = sumX/frequency;
                centers[j][1] = sumY/frequency;
            }
        }

        //Возвращаем массив индексов центров для каждой точки И массив точек-центров кластеров
        return new Pair<>(result, centers);
    }

    public int[] getResultArray() {
        return resultArray;
    }

    public double[][] getCentersArray() {
        return centersArray;
    }

}
