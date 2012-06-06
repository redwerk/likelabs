#import "Review.h"
#import "XMLWriter.h"
#import "Base64.h"

@implementation Review
NSUInteger const MAX_CONTACTS = 5;
static NSUInteger const MAX_PHOTO = 5;
static NSString* const USER_KEY = @"user";
static NSString* const REVIEW_PHOTO_INDEX_KEY = @"reviewPhotoIndex";
static NSString* const PHOTOS_KEY = @"photos";
static NSString* const TEXT_KEY = @"text";
static NSString* const REVIEW_TYPE_KEY = @"reviewType";
static NSString* const CONTACTS_KEY = @"contacts";
static NSString* const IMAGE_URL_KEY = @"imageUrl";

@synthesize user = _user;
@synthesize reviewPhotoIndex = _reviewPhotoIndex;
@synthesize reviewType = _reviewType;
@synthesize photos = _photos;
@synthesize text = _text;
@synthesize contacts = _contacts;
@synthesize imageUrl = _imageUrl;

#pragma mark - Initialization

- (id) init {
    if (self = [super init]) {
        _photos = [[NSMutableArray alloc] initWithCapacity:MAX_PHOTO];
        _user = [[User alloc] init];
        _contacts = [[NSMutableArray alloc] initWithCapacity:MAX_CONTACTS];
    }
    return self;
}

- (id)initWithReviewType:(ReviewType)reviewType {
    if (self = [self init]) {
        self.reviewType = reviewType;
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

#pragma mark - Photo index management

- (void)setReviewPhotoIndex:(NSInteger)reviewPhotoIndex {
    Photo* oldPhoto = [self.photos objectAtIndex:_reviewPhotoIndex];
    oldPhoto.status = PhotoStatusActive;
    
    Photo* newPhoto = [self.photos objectAtIndex:reviewPhotoIndex];
    newPhoto.status = PhotoStatusSelected;
    
    _reviewPhotoIndex = reviewPhotoIndex;
}

#pragma mark - Serialization

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
    NSString* xml = [NSString stringWithString:[xmlWriter toString]];
    [xmlWriter release];
    return xml;
}

- (void)encodeWithCoder:(NSCoder *)aCoder {
    [aCoder encodeObject:self.user forKey:USER_KEY];
    [aCoder encodeObject:[NSNumber numberWithInteger:self.reviewPhotoIndex] forKey:REVIEW_PHOTO_INDEX_KEY];    
    [aCoder encodeObject:self.photos forKey:PHOTOS_KEY];
    [aCoder encodeObject:self.text forKey:TEXT_KEY];
    [aCoder encodeObject:[NSNumber numberWithUnsignedInt: self.reviewType] forKey:REVIEW_TYPE_KEY];
    [aCoder encodeObject:self.contacts forKey:CONTACTS_KEY];
    [aCoder encodeObject:self.imageUrl forKey:IMAGE_URL_KEY];
}

- (id)initWithCoder:(NSCoder *)aDecoder {
    if (self = [super init]) {
        self.user = [aDecoder decodeObjectForKey:USER_KEY];
        self.reviewPhotoIndex = ((NSNumber*)[aDecoder decodeObjectForKey:REVIEW_PHOTO_INDEX_KEY]).integerValue;
        self.photos = [aDecoder decodeObjectForKey:PHOTOS_KEY];
        self.text = [aDecoder decodeObjectForKey:TEXT_KEY];
        self.reviewType = ((NSNumber*)[aDecoder decodeObjectForKey:REVIEW_TYPE_KEY]).unsignedIntegerValue;
        self.contacts = [aDecoder decodeObjectForKey:CONTACTS_KEY];
        self.imageUrl = [aDecoder decodeObjectForKey:IMAGE_URL_KEY];
    }
    return self;
}


#pragma mark - Memory mangement

- (void)dealloc {
    self.photos = nil;
    self.text = nil;
    self.user = nil;
    self.contacts = nil;
    self.imageUrl = nil;
    [super dealloc];
}
@end
