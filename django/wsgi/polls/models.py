import datetime
from django.utils import timezone
from django.db import models
from django import forms
import os

# Create your models here.


class Item(models.Model):
    price = models.IntegerField()
    pic = models.CharField(max_length=30)
