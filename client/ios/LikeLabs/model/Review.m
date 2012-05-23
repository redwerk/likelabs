#import "Review.h"
#import "XMLWriter.h"
#import "Base64.h"

@implementation Review
NSUInteger const MAX_CONTACTS = 5;

@synthesize user = _user;
@synthesize reviewPhotoIndex = _reviewPhotoIndex;
@synthesize reviewType = _reviewType;
@synthesize photos = _photos;
@synthesize text = _text;
@synthesize contacts = _contacts;

- (id)initWithReviewType:(ReviewType)reviewType {
    if (self = [super init]) {
        self.reviewType = reviewType;
        _photos = [[NSMutableArray alloc] init];
        _user = [[User alloc] init];
        _contacts = [[NSMutableArray alloc] initWithCapacity:MAX_CONTACTS];
    }
    return self;
}

- (id)initWithUser:(User*)user andText:(NSString*)text {
    if (self = [self initWithReviewType: ReviewTypeText]) {
        self.user = user;
        self.text = text;
    }
    return self;
}

- (void)setReviewPhotoIndex:(NSInteger)reviewPhotoIndex {
    Photo* oldPhoto = [self.photos objectAtIndex:_reviewPhotoIndex];
    oldPhoto.status = PhotoStatusActive;
    
    Photo* newPhoto = [self.photos objectAtIndex:reviewPhotoIndex];
    newPhoto.status = PhotoStatusSelected;
    
    _reviewPhotoIndex = reviewPhotoIndex;
}

- (NSString*) serializeToXml {
    XMLWriter* xmlWriter = [[XMLWriter alloc] init];
    [xmlWriter writeStartElement:@"review"];
    
    [xmlWriter writeStartElement:@"authorPhone"];
    if (self.user.phone) {
        [xmlWriter writeCharacters:self.user.phone];
    }
    [xmlWriter writeEndElement];//authorPhone
    
    [xmlWriter writeStartElement:@"text"];
    if (self.text) {
        [xmlWriter writeCharacters:self.text];
    }
    [xmlWriter writeEndElement];//text
    
    if (self.reviewType == ReviewTypePhoto) {
        [Base64 initialize];
        [xmlWriter writeStartElement:@"photos"];   
        for (Photo* photo in self.photos) {
            [xmlWriter writeStartElement:@"photo"];
            
            [xmlWriter writeStartElement:@"image"];
            NSData* imgData = UIImageJPEGRepresentation(photo.image, 100);
            NSString* encodedData = [Base64 encode:imgData];
            [xmlWriter writeCharacters:encodedData];
            [xmlWriter writeEndElement];//image
            
            [xmlWriter writeStartElement:@"status"];
            switch (photo.status) {
                case PhotoStatusActive:
                    [xmlWriter writeCharacters:@"ACTIVE"];
                    break;
                case PhotoStatusDeleted:
                    [xmlWriter writeCharacters:@"DELETED"];
                    break;
                case PhotoStatusSelected:
                    [xmlWriter writeCharacters:@"SELECTED"];
                    break;
            }
            [xmlWriter writeEndElement];//status
            
            [xmlWriter writeEndElement];//photo
        }
        [xmlWriter writeEndElement];//photos
    }
    
    [xmlWriter writeStartElement:@"recipients"];
    for (Contact* contact in self.contacts) {
        [xmlWriter writeStartElement:@"recipient"];
        
        [xmlWriter writeStartElement:@"type"];
        [xmlWriter writeCharacters:(contact.contactType == ContactTypeEmail) ? @"EMAIL" : @"SMS"];
        [xmlWriter writeEndElement];//type
        
        [xmlWriter writeStartElement:@"address"];
        [xmlWriter writeCharacters:contact.contactString];
        [xmlWriter writeEndElement];//address
        
        [xmlWriter writeEndElement];//recipient
    }
    [xmlWriter writeEndElement];//recipients
    
    [xmlWriter writeEndElement];//review
    [xmlWriter writeEndDocument];//just in case
    NSString* xml = [xmlWriter toString];
    [xmlWriter release];
    return xml;
}

- (void)dealloc {
    self.photos = nil;
    self.text = nil;
    self.user = nil;
    self.contacts = nil;
    [_photos release];
    [_text release];
    [_user release];
    [_contacts release];
    [super dealloc];
}

@end
