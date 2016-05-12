from django.conf.urls import patterns, url
from django.conf import settings
from django.conf.urls.static import static
from login import views

urlpatterns = patterns('',
    # ex: /polls/
     url(r'^$', views.index, name='index'),
    # ex: /polls/5/
    url(r'^create/$', views.create, name='create'),
    

)
# + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)