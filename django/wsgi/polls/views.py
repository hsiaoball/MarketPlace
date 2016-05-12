from django.http import HttpResponseRedirect, HttpResponse
from polls.models import Item
from django.views.decorators.csrf import csrf_exempt
import os
import datetime
from django.core import serializers
import json


@csrf_exempt
def catalog(request):
    items_list= Item.objects.all()
    serialized_obj = serializers.serialize('json', items_list)	
    return HttpResponse(serialized_obj, content_type='application/json')
