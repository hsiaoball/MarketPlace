from django.views.decorators.csrf import csrf_exempt
import json
from django.http import HttpResponseRedirect, HttpResponse
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login, logout

import os
from django.conf import settings

from django.core import serializers

@csrf_exempt	
def index(request):
    if 'username' not in request.POST:
        return HttpResponse(json.dumps({"error": "No username"}), content_type='application/json')	
    username = request.POST['username']
    password = request.POST['pw']
    user = authenticate(username=username, password=password)
    if user is not None:
        login(request, user)
        return HttpResponse(json.dumps({"id": user.id,"username": username}), content_type='application/json')	
    else:
        return HttpResponse(json.dumps({"error": "No exist user"}), content_type='application/json')	
        
            

@csrf_exempt
def create(request):
    if 'username' in request.POST:
        username=request.POST['username']  
        password=request.POST['pw']
        if User.objects.filter(username=username).exists():    
            return HttpResponse(json.dumps({"error": "user exist"}), content_type='application/json')	
        else:
            user = User.objects.create_user(username=request.POST['username'],
                                                  password=request.POST['pw'])
            user.save()                                      
            user = authenticate(username=username, password=password)
            login(request, user)
            return HttpResponse(json.dumps({"id": user.id,"username": username}), content_type='application/json')	
    else:
        return HttpResponse(json.dumps({"error": "No username"}), content_type='application/json')	
