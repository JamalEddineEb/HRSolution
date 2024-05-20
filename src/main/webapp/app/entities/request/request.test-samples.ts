import { IRequest, NewRequest } from './request.model';

export const sampleWithRequiredData: IRequest = {
  id: 29848,
  status: 'ACCEPTED',
};

export const sampleWithPartialData: IRequest = {
  id: 32212,
  status: 'PROCESSING',
};

export const sampleWithFullData: IRequest = {
  id: 22956,
  status: 'REVOKED',
};

export const sampleWithNewData: NewRequest = {
  status: 'ACCEPTED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
