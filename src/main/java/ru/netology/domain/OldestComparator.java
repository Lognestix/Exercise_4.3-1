package ru.netology.domain;

import java.util.Comparator;

public class OldestComparator implements Comparator<Issue> {
  public int compare(Issue t1, Issue t2) {
    return t2.getLifetime() - t1.getLifetime();
  }
}