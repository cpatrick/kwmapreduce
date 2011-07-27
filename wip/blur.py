from scipy import *
from numpy import *
from scipy import signal
import sys
import Image

def gauss_kern(size, sizey=None):
    """ Returns a normalized 2D gauss kernel array for convolutions """
    size = int(size)
    if not sizey:
        sizey = size
    else:
        sizey = int(sizey)
    x, y = mgrid[-size:size+1, -sizey:sizey+1]
    g = exp(-(x**2/float(size)+y**2/float(sizey)))
    return g / g.sum()

def blur_image(im, n, ny=None) :
    """ blurs the image by convolving with a gaussian kernel of typical
        size n. The optional keyword argument ny allows for a different
        size in the y direction.
    """
    g = gauss_kern(n, sizey=ny)
    improc = signal.convolve(im,g, mode='valid')
    return(improc)

if __name__ == '__main__':
    I = asarray(Image.open(sys.argv[1]))
    r = I[:,:,0]
    g = I[:,:,1]
    b = I[:,:,2]
    gray = r*.222 + g*.7067 + b*.0713
    print gray
    out = blur_image(gray,20)
    im = Image.fromarray(uint8(out))
    im.save('blur.jpg')
