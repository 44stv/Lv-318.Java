import {Stop} from './stop.model';

export class Feedback {
  userId: number;
  transitId: number;
  criteriaId: number;
  type: String;
  questions: String[];
  answer: String;
}

export class FeedbackDTO {
  userId: number;
  transitId: number;
  criteriaId: number;
  answer: any;
}

export class RatingAnswer {
  answer: number;
  weight: number;
}

export class Answer {
  value: string;
}

export class CapacityAnswer {
  from: Stop;
  to: Stop;
  capacity: number;
  startTime: Time;
  endTime: Time;

  toCapacityHoursString(): string {
    const answer = '';
    return this.startTime.toString() + this.endTime.toString() + this.capacity.toString();
  }

  toCapacityRouteString(): string {
    const answer = '';
    return this.from.toString() + this.endTime.toString() + this.capacity.toString();
  }
}

export class Time {
  hour: number;
  minute: number;
}
